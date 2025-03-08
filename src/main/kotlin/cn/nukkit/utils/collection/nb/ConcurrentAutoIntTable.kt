package cn.nukkit.utils.collection.nb

import java.io.Serializable
import java.lang.invoke.MethodHandles
import java.lang.invoke.VarHandle
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater
import kotlin.concurrent.Volatile

/**
 * An auto-resizing table of `longs`, supporting low-contention CAS
 * operations.  Updates are done with CAS's to no particular table element.
 * The intent is to support highly scalable counters, r/w locks, and other
 * structures where the updates are associative, loss-free (no-brainer), and
 * otherwise happen at such a high volume that the cache contention for
 * CAS'ing a single word is unacceptable.
 *
 * @author Cliff Click, SuperIceCN
 * @since 1.5
 */
class ConcurrentAutoIntTable : Serializable {
    // --- public interface ---
    /**
     * Add the given value to current counter value.  Concurrent updates will
     * not be lost, but addAndGet or getAndAdd are not implemented because the
     * total counter value (i.e., [.get]) is not atomically updated.
     * Updates are striped across an array of counters to avoid cache contention
     * and has been tested with performance scaling linearly up to 768 CPUs.
     */
    fun add(x: Int) {
        add_if(x)
    }

    /**
     * [.add] with -1
     */
    fun decrement() {
        add_if(-1)
    }

    /**
     * [.add] with +1
     */
    fun increment() {
        add_if(1)
    }

    /**
     * Atomically set the sum of the striped counters to specified value.
     * Rather more expensive than a simple store, in order to remain atomic.
     */
    fun set(x: Int) {
        val newcat = CAT(null, 4, x)
        // Spin until CAS works
        while (!CAS_cat(_cat, newcat)) { /*empty*/
        }
    }

    /**
     * Current value of the counter.  Since other threads are updating furiously
     * the value is only approximate, but it includes all counts made by the
     * current thread.  Requires a pass over the internally striped counters.
     */
    fun get(): Long {
        return _cat.sum()
    }

    /**
     * Same as [.get], included for completeness.
     */
    fun intValue(): Int {
        return _cat.sum().toInt()
    }

    /**
     * Same as [.get], included for completeness.
     */
    fun longValue(): Long {
        return _cat.sum()
    }

    /**
     * A cheaper [.get].  Updated only once/millisecond, but as fast as a
     * simple load instruction when not updating.
     */
    fun estimate_get(): Long {
        return _cat.estimate_sum()
    }

    /**
     * Return the counter's `long` value converted to a string.
     */
    override fun toString(): String {
        return _cat.toString()
    }

    /**
     * Return the internal counter striping factor.  Useful for diagnosing
     * performance problems.
     */
    fun internal_size(): Int {
        return _cat._t.size
    }

    // Only add 'x' to some slot in table, hinted at by 'hash'.  The sum can
    // overflow.  Value is CAS'd so no counts are lost.  The CAS is retried until
    // it succeeds.  Returned value is the old value.
    private fun add_if(x: Int): Long {
        return _cat.add_if(x, hash(), this).toLong()
    }

    // The underlying array of concurrently updated long counters
    @Volatile
    private var _cat = CAT(null, 16,  /*Start Small, Think Big!*/0)
    private fun CAS_cat(oldcat: CAT, newcat: CAT): Boolean {
        return _catUpdater.compareAndSet(this, oldcat, newcat)
    }

    // --- CAT -----------------------------------------------------------------
    private class CAT(//volatile long _resizers;    // count of threads attempting a resize
        //static private final AtomicLongFieldUpdater<CAT> _resizerUpdater =
        //  AtomicLongFieldUpdater.newUpdater(CAT.class, "_resizers");
        private val _next: CAT?, sz: Int, init: Int
    ) :
        Serializable {
        @Volatile
        private var _fuzzy_sum_cache: Long = 0

        @Volatile
        private var _fuzzy_time: Long = 0
        val _t: IntArray = IntArray(sz) // Power-of-2 array of ints

        init {
            _t[0] = init
        }

        // Only add 'x' to some slot in table, hinted at by 'hash'.  The sum can
        // overflow.  Value is CAS'd so no counts are lost.  The CAS is attempted
        // ONCE.
        fun add_if(x: Int, hash: Int, master: ConcurrentAutoIntTable): Int {
            val t = _t
            val idx = hash and (t.size - 1)
            // Peel loop; try once fast
            var old = t[idx]
            val ok = CAS(t, idx, old, old + x)
            if (ok) return old // Got it

            // Try harder
            var cnt = 0
            while (true) {
                old = t[idx]
                if (CAS(t, idx, old, old + x)) break // Got it!

                cnt++
            }
            if (cnt < MAX_SPIN) return old // Allowable spin loop count

            if (t.size >= 1024 * 1024) return old // too big already


            // Too much contention; double array size in an effort to reduce contention
            //long r = _resizers;
            //final int newbytes = (t.length<<1)<<3/*word to bytes*/;
            //while( !_resizerUpdater.compareAndSet(this,r,r+newbytes) )
            //  r = _resizers;
            //r += newbytes;
            if (master._cat !== this) return old // Already doubled, don't bother


            //if( (r>>17) != 0 ) {      // Already too much allocation attempts?
            //  // We could use a wait with timeout, so we'll wakeup as soon as the new
            //  // table is ready, or after the timeout in any case.  Annoyingly, this
            //  // breaks the non-blocking property - so for now we just briefly sleep.
            //  //synchronized( this ) { wait(8*megs); }         // Timeout - we always wakeup
            //  try { Thread.sleep(r>>17); } catch( InterruptedException e ) { }
            //  if( master._cat != this ) return old;
            //}
            val newcat = CAT(this, t.size * 2, 0)
            // Take 1 stab at updating the CAT with the new larger size.  If this
            // fails, we assume some other thread already expanded the CAT - so we
            // do not need to retry until it succeeds.
            while (master._cat === this && !master.CAS_cat(this, newcat)) { /*empty*/
            }
            return old
        }

        // Return the current sum of all things in the table.  Writers can be
        // updating the table furiously, so the sum is only locally accurate.
        fun sum(): Long {
            var sum = _next?.sum() ?: 0 // Recursively get cached sum
            val t = _t
            for (cnt in t) sum += cnt.toLong()
            return sum
        }

        // Fast fuzzy version.  Used a cached value until it gets old, then re-up
        // the cache.
        fun estimate_sum(): Long {
            // For short tables, just do the work
            if (_t.size <= 64) return sum()
            // For bigger tables, periodically freshen a cached value
            val millis = System.currentTimeMillis()
            if (_fuzzy_time != millis) { // Time marches on?
                _fuzzy_sum_cache = sum() // Get sum the hard way
                _fuzzy_time = millis // Indicate freshness of cached value
            }
            return _fuzzy_sum_cache // Return cached sum
        }

        override fun toString(): String {
            return sum().toString()
        }

        companion object {
            private val _IHandle: VarHandle = MethodHandles.arrayElementVarHandle(
                IntArray::class.java
            )

            private fun CAS(A: IntArray, idx: Int, old: Int, nnn: Int): Boolean {
                return _IHandle.compareAndSet(A, idx, old, nnn)
            }

            private const val MAX_SPIN = 1
        }
    }

    companion object {
        private val _catUpdater: AtomicReferenceFieldUpdater<ConcurrentAutoIntTable, CAT> =
            AtomicReferenceFieldUpdater.newUpdater(
                ConcurrentAutoIntTable::class.java,
                CAT::class.java, "_cat"
            )

        // Hash spreader
        private fun hash(): Int {
            //int h = (int)Thread.currentThread().getId();
            val h = System.identityHashCode(Thread.currentThread())
            return h shl 3 // Pad out cache lines.  The goal is to avoid cache-line contention
        }
    }
}
