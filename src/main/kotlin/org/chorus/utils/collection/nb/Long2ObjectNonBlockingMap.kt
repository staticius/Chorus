package org.chorus.utils.collection.nb

import org.chorus.api.NonComputationAtomic
import it.unimi.dsi.fastutil.longs.AbstractLongSet
import it.unimi.dsi.fastutil.longs.LongIterator
import it.unimi.dsi.fastutil.longs.LongSet
import java.io.*
import java.lang.invoke.MethodHandles
import java.lang.invoke.VarHandle
import java.util.*
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.atomic.AtomicLongFieldUpdater
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater
import java.util.function.BiFunction
import java.util.function.Function
import kotlin.concurrent.Volatile
import kotlin.math.min

/**
 * A lock-free alternate implementation of [java.util.concurrent.ConcurrentHashMap]
 * with **primitive long keys**, better scaling properties and
 * generally lower costs.  The use of `long` keys allows for faster
 * compares and lower memory costs.  The Map provides identical correctness
 * properties as ConcurrentHashMap.  All operations are non-blocking and
 * multi-thread safe, including all update operations.  [ ] scales substatially better than [ ] for high update rates, even with a large
 * concurrency factor.  Scaling is linear up to 768 CPUs on a 768-CPU Azul
 * box, even with 100% updates or 100% reads or any fraction in-between.
 * Linear scaling up to all cpus has been observed on a 32-way Sun US2 box,
 * 32-way Sun Niagra box, 8-way Intel box and a 4-way Power box.
 *
 *
 * **The main benefit of this class** over using plain [ ] with [Long] keys is
 * that it avoids the auto-boxing and unboxing costs.  Since auto-boxing is
 * *automatic*, it is easy to accidentally cause auto-boxing and negate
 * the space and speed benefits.
 *
 *
 * This class obeys the same functional specification as [ ], and includes versions of methods corresponding to
 * each method of <tt>Hashtable</tt>.  However, even though all operations are
 * thread-safe, operations do *not* entail locking and there is
 * *not* any support for locking the entire table in a way that
 * prevents all access.  This class is fully interoperable with
 * <tt>Hashtable</tt> in programs that rely on its thread safety but not on
 * its synchronization details.
 *
 *
 *  Operations (including <tt>put</tt>) generally do not block, so may
 * overlap with other update operations (including other <tt>puts</tt> and
 * <tt>removes</tt>).  Retrievals reflect the results of the most recently
 * *completed* update operations holding upon their onset.  For
 * aggregate operations such as <tt>putAll</tt>, concurrent retrievals may
 * reflect insertion or removal of only some entries.  Similarly, Iterators
 * and Enumerations return elements reflecting the state of the hash table at
 * some point at or since the creation of the iterator/enumeration.  They do
 * *not* throw [ConcurrentModificationException].  However,
 * iterators are designed to be used by only one thread at a time.
 *
 *
 *  Very full tables, or tables with high re-probe rates may trigger an
 * internal resize operation to move into a larger table.  Resizing is not
 * terribly expensive, but it is not free either; during resize operations
 * table throughput may drop somewhat.  All threads that visit the table
 * during a resize will 'help' the resizing but will still be allowed to
 * complete their operation before the resize is finished (i.e., a simple
 * 'get' operation on a million-entry table undergoing resizing will not need
 * to block until the entire million entries are copied).
 *
 *
 * This class and its views and iterators implement all of the
 * *optional* methods of the [Map] and [Iterator]
 * interfaces.
 *
 *
 *  Like [Hashtable] but unlike [HashMap], this class
 * does *not* allow <tt>null</tt> to be used as a value.
 *
 * @param <TypeV> the type of mapped values
 * @author Cliff Click, SuperIceCN
 * @since 1.5
</TypeV> */
@NonComputationAtomic
class Long2ObjectNonBlockingMap<TypeV>
@JvmOverloads constructor(initial_sz: Int = MIN_SIZE, opt_for_space: Boolean = true) :
    AbstractMap<Long, TypeV?>(), ConcurrentMap<Long, TypeV?>, Cloneable, Serializable {
    private fun CAS(handle: VarHandle, old: Any?, nnn: Any): Boolean {
        return handle.compareAndSet(this, old, nnn)
    }

    // --- Adding a 'prime' bit onto Values via wrapping with a junk wrapper class
    private class Prime(V: Any) {
        val _V: Any = V

        companion object {
            fun unbox(V: Any?): Any {
                return (if (V is Prime) V._V else V)!!
            }
        }
    }

    // --- The Hash Table --------------------
    @Transient
    private var _chm: CHM? = null

    // This next field holds the value for Key 0 - the special key value which
    // is the initial array value, and also means: no-key-inserted-yet.
    @Transient
    private var _val_1: Any? = null // Value for Key: NO_KEY

    // Time since last resize
    @Transient
    private var _last_resize_milli: Long = 0

    // Optimize for space: use a 1/2-sized table and allow more re-probes
    private val _opt_for_space = opt_for_space

    // Count of reprobes
    @Transient
    private var _reprobes = ConcurrentAutoLongTable()

    /**
     * Get and clear the current count of reprobes.  Reprobes happen on key
     * collisions, and a high reprobe rate may indicate a poor hash function or
     * weaknesses in the table resizing function.
     *
     * @return the count of reprobes since the last call to [.reprobes]
     * or since the table was created.
     */
    fun reprobes(): Long {
        val r = _reprobes.get()
        _reprobes = ConcurrentAutoLongTable()
        return r
    }

    /**
     * Create a new Long2ObjectNonBlockingMap, setting the space-for-speed
     * tradeoff.  `true` optimizes for space and is the default.  `false` optimizes for speed and doubles space costs for roughly a 10%
     * speed improvement.
     */
    constructor(opt_for_space: Boolean) : this(1, opt_for_space)

    /**
     * Create a new Long2ObjectNonBlockingMap, setting both the initial size and
     * the space-for-speed tradeoff.  `true` optimizes for space and is
     * the default.  `false` optimizes for speed and doubles space costs
     * for roughly a 10% speed improvement.
     */
    // --- Long2ObjectNonBlockingMap ----------------------------------------------
    // Constructors
    /**
     * Create a new Long2ObjectNonBlockingMap with default minimum size (currently set
     * to 8 K/V pairs or roughly 84 bytes on a standard 32-bit JVM).
     */
    /**
     * Create a new Long2ObjectNonBlockingMap with initial room for the given
     * number of elements, thus avoiding internal resizing operations to reach
     * an appropriate size.  Large numbers here when used with a small count of
     * elements will sacrifice space for a small amount of time gained.  The
     * initial size will be rounded up internally to the next larger power of 2.
     */
    init {
        initialize(initial_sz)
    }

    private fun initialize(initial_sz: Int) {
        RangeUtil.checkPositiveOrZero(initial_sz, "initial_sz")
        var i = MIN_SIZE_LOG // Convert to next largest power-of-2
        while ((1 shl i) < initial_sz) {
            i++
        }
        _chm = CHM(this, ConcurrentAutoLongTable(), i)
        _val_1 = TOMBSTONE // Always as-if deleted
        _last_resize_milli = System.currentTimeMillis()
    }

    // --- wrappers ------------------------------------------------------------
    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    override fun size(): Int {
        return (if (_val_1 === TOMBSTONE) 0 else 1) + _chm!!.size()
    }

    /**
     * Tests if the key in the table.
     *
     * @return <tt>true</tt> if the key is in the table
     */
    override fun containsKey(key: Long): Boolean {
        return get(key) != null
    }

    /**
     * Legacy method testing if some key maps into the specified value in this
     * table.  This method is identical in functionality to [ ][.containsValue], and exists solely to ensure full compatibility with
     * class [Hashtable], which supported this method prior to
     * introduction of the Java Collections framework.
     *
     * @param val a value to search for
     * @return <tt>true</tt> if this map maps one or more keys to the specified value
     * @throws NullPointerException if the specified value is null
     */
    fun contains(`val`: Any?): Boolean {
        return containsValue(`val`)
    }

    /**
     * Maps the specified key to the specified value in the table.  The value
     * cannot be null.
     *
     * The value can be retrieved by calling [.get]
     * with a key that is equal to the original key.
     *
     * @param key key with which the specified value is to be associated
     * @param val value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>
     * @throws NullPointerException if the specified value is null
     */
    fun put(key: Long, `val`: TypeV): TypeV? {
        return putIfMatch(key, `val`, NO_MATCH_OLD)
    }

    /**
     * Atomically, do a [.put] if-and-only-if the key is not mapped.
     * Useful to ensure that only a single mapping for the key exists, even if
     * many threads are trying to create the mapping in parallel.
     *
     * @return the previous value associated with the specified key,
     * or <tt>null</tt> if there was no mapping for the key
     * @throws NullPointerException if the specified is value is null
     */
    fun putIfAbsent(key: Long, `val`: TypeV): TypeV? {
        return putIfMatch(key, `val`, TOMBSTONE)
    }

    /**
     * Removes the key (and its corresponding value) from this map.
     * This method does nothing if the key is not in the map.
     *
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>
     */
    override fun remove(key: Long): TypeV? {
        return putIfMatch(key, TOMBSTONE, NO_MATCH_OLD)
    }

    /**
     * Atomically do a [.remove] if-and-only-if the key is mapped
     * to a value which is `equals` to the given value.
     *
     * @throws NullPointerException if the specified value is null
     */
    fun remove(key: Long, `val`: Any): Boolean {
        return putIfMatch(key, TOMBSTONE, `val`) === `val`
    }

    /**
     * Atomically do a `put(key,val)` if-and-only-if the key is
     * mapped to some value already.
     *
     * @throws NullPointerException if the specified value is null
     */
    fun replace(key: Long, `val`: TypeV): TypeV? {
        return putIfMatch(key, `val`, MATCH_ANY)
    }

    /**
     * Atomically do a `put(key,newValue)` if-and-only-if the key is
     * mapped a value which is `equals` to `oldValue`.
     *
     * @throws NullPointerException if the specified value is null
     */
    fun replace(key: Long, oldValue: TypeV, newValue: TypeV): Boolean {
        return putIfMatch(key, newValue, oldValue) === oldValue
    }

    private fun putIfMatch(key: Long, newVal: Any, oldVal: Any): TypeV? {
        if (oldVal == null || newVal == null) throw NullPointerException()
        if (key == NO_KEY) {
            var curVal = _val_1
            if (oldVal === NO_MATCH_OLD ||  // Do we care about expected-Value at all?
                curVal === oldVal ||  // No instant match already?
                (oldVal === MATCH_ANY && curVal !== TOMBSTONE) ||
                oldVal == curVal
            ) { // Expensive equals check
                if (!CAS(_val_1_handler!!, curVal, newVal))  // One shot CAS update attempt
                    curVal = _val_1 // Failed; get failing witness
            }
            return if (curVal === TOMBSTONE) null else curVal as TypeV? // Return the last value present
        }
        val res = _chm!!.putIfMatch(key, newVal, oldVal)
        assert(res !is Prime)
        checkNotNull(res)
        return if (res === TOMBSTONE) null else res as TypeV
    }

    /**
     * Removes all of the mappings from this map.
     */
    override fun clear() {         // Smack a new empty table down
        val newchm = CHM(this, ConcurrentAutoLongTable(), MIN_SIZE_LOG)
        while (!CAS(_chm_handler!!, _chm, newchm)) { /*Spin until the clear works*/
        }
        CAS(_val_1_handler!!, _val_1, TOMBSTONE)
    }

    // Non-atomic clear, preserving existing large arrays
    fun clear(large: Boolean) {         // Smack a new empty table down
        _chm!!.clear()
        CAS(_val_1_handler!!, _val_1, TOMBSTONE)
    }

    /**
     * Returns <tt>true</tt> if this Map maps one or more keys to the specified
     * value.  *Note*: This method requires a full internal traversal of the
     * hash table and is much slower than [.containsKey].
     *
     * @param val value whose presence in this map is to be tested
     * @return <tt>true</tt> if this Map maps one or more keys to the specified value
     * @throws NullPointerException if the specified value is null
     */
    override fun containsValue(`val`: Any?): Boolean {
        if (`val` == null) return false
        if (`val` === _val_1) return true // Key 0

        for (V in values) if (V === `val` || V == `val`) return true
        return false
    }

    // --- get -----------------------------------------------------------------
    /**
     * Returns the value to which the specified key is mapped, or `null`
     * if this map contains no mapping for the key.
     *
     * More formally, if this map contains a mapping from a key `k` to
     * a value `v` such that `key==k`, then this method
     * returns `v`; otherwise it returns `null`.  (There can be at
     * most one such mapping.)
     *
     * @throws NullPointerException if the specified key is null
     */
    // Never returns a Prime nor a Tombstone.
    override fun get(key: Long): TypeV? {
        if (key == NO_KEY) {
            val V = _val_1
            return if (V === TOMBSTONE) null else V as TypeV?
        }
        val V = _chm!!.get_impl(key)
        assert(
            V !is Prime // Never return a Prime
        )
        assert(V !== TOMBSTONE)
        return V as TypeV?
    }

    /**
     * Auto-boxing version of [.get].
     */
    override fun get(key: Any): TypeV? {
        return if (key is Long) get(key) else null
    }

    /**
     * Auto-boxing version of [.remove].
     */
    override fun remove(key: Any): TypeV? {
        return if (key is Long) remove(key) else null
    }

    /**
     * Auto-boxing version of [.remove].
     */
    override fun remove(key: Any, Val: Any): Boolean {
        return (key is Long) && remove(key, Val)
    }

    /**
     * Auto-boxing version of [.containsKey].
     */
    override fun containsKey(key: Any): Boolean {
        return (key is Long) && containsKey(key)
    }

    /**
     * Auto-boxing version of [.putIfAbsent].
     */
    override fun putIfAbsent(key: Long, `val`: TypeV): TypeV? {
        return putIfAbsent(key, `val`)
    }

    /**
     * Auto-boxing version of [.replace].
     */
    override fun replace(key: Long, Val: TypeV): TypeV? {
        return replace(key, Val)
    }

    /**
     * Auto-boxing version of [.put].
     */
    override fun put(key: Long, `val`: TypeV?): TypeV? {
        return put(key, `val`)
    }

    /**
     * Auto-boxing version of [.replace].
     */
    override fun replace(key: Long, oldValue: TypeV, newValue: TypeV): Boolean {
        return replace(key, oldValue, newValue)
    }

    // --- help_copy -----------------------------------------------------------
    // Help along an existing resize operation.  This is just a fast cut-out
    // wrapper, to encourage inlining for the fast no-copy-in-progress case.  We
    // always help the top-most table copy, even if there are nested table
    // copies in progress.
    private fun help_copy() {
        // Read the top-level CHM only once.  We'll try to help this copy along,
        // even if it gets promoted out from under us (i.e., the copy completes
        // and another KVS becomes the top-level copy).
        val topchm = _chm
        if (topchm!!._newchm == null) return  // No copy in-progress

        topchm.help_copy_impl(false)
    }

    // --- CHM -----------------------------------------------------------------
    // The control structure for the Long2ObjectNonBlockingMap
    private class CHM(nbhml: Long2ObjectNonBlockingMap<*>, size: ConcurrentAutoLongTable, logsize: Int) : Serializable {
        // Back-pointer to top-level structure
        val _nbhml: Long2ObjectNonBlockingMap<*> = nbhml

        // Size in active K,V pairs
        private var _size: ConcurrentAutoLongTable

        fun size(): Int {
            return _size.get().toInt()
        }

        // ---
        // These next 2 fields are used in the resizing heuristics, to judge when
        // it is time to resize or copy the table.  Slots is a count of used-up
        // key slots, and when it nears a large fraction of the table we probably
        // end up reprobing too much.  Last-resize-milli is the time since the
        // last resize; if we are running back-to-back resizes without growing
        // (because there are only a few live keys but many slots full of dead
        // keys) then we need a larger table to cut down on the churn.
        // Count of used slots, to tell when table is full of dead unusable slots
        private var _slots: ConcurrentAutoLongTable

        fun slots(): Int {
            return _slots.get().toInt()
        }

        // ---
        // New mappings, used during resizing.
        // The 'next' CHM - created during a resize operation.  This represents
        // the new table being copied from the old one.  It's the volatile
        // variable that is read as we cross from one table to the next, to get
        // the required memory orderings.  It monotonically transits from null to
        // set (once).
        @Volatile
        var _newchm: CHM? = null

        // Set the _newchm field if we can.  AtomicUpdaters do not fail spuriously.
        fun CAS_newchm(newchm: CHM?): Boolean {
            return _newchmUpdater.compareAndSet(
                this, null, newchm
            )
        }

        // Sometimes many threads race to create a new very large table.  Only 1
        // wins the race, but the losers all allocate a junk large table with
        // hefty allocation costs.  Attempt to control the overkill here by
        // throttling attempts to create a new table.  I cannot really block here
        // (lest I lose the non-blocking property) but late-arriving threads can
        // give the initial resizing thread a little time to allocate the initial
        // new table.  The Right Long Term Fix here is to use array-lets and
        // incrementally create the new very large array.  In C I'd make the array
        // with malloc (which would mmap under the hood) which would only eat
        // virtual-address and not real memory - and after Somebody wins then we
        // could in parallel initialize the array.  Java does not allow
        // un-initialized array creation (especially of ref arrays!).
        @Volatile
        var _resizers: Long = 0 // count of threads attempting an initial resize

        // --- key,val -------------------------------------------------------------
        // Access K,V for a given idx
        fun CAS_key(idx: Int, old: Long, key: Long): Boolean {
            // return UNSAFE.compareAndSwapLong(_keys, rawIndex(_keys, idx), old, key);
            return _LHandler.compareAndSet(_keys, idx, old, key)
        }

        fun CAS_val(idx: Int, old: Any?, `val`: Any?): Boolean {
            // return UNSAFE.compareAndSwapObject(_vals, rawIndex(_vals, idx), old, val);
            return _OHandler.compareAndSet(_vals, idx, old, `val`)
        }

        val _keys: LongArray
        val _vals: Array<Any?>

        // Non-atomic clear
        fun clear() {
            _size = ConcurrentAutoLongTable()
            _slots = ConcurrentAutoLongTable()
            Arrays.fill(_keys, 0)
            Arrays.fill(_vals, null)
        }

        // --- get_impl ----------------------------------------------------------
        // Never returns a Prime nor a Tombstone.
        fun get_impl(key: Long): Any? {
            val hash = hash(key)
            val len = _keys.size
            var idx = (hash and (len - 1)) // First key hash

            // Main spin/reprobe loop, looking for a Key hit
            var reprobe_cnt = 0
            while (true) {
                val K = _keys[idx] // Get key   before volatile read, could be NO_KEY
                val V = _vals[idx] // Get value before volatile read, could be null or Tombstone or Prime
                if (K == NO_KEY) return null // A clear miss


                // Key-compare
                if (key == K) {
                    // Key hit!  Check for no table-copy-in-progress
                    if (V !is Prime) { // No copy?
                        if (V === TOMBSTONE) return null
                        // We need a volatile-read between reading a newly inserted Value
                        // and returning the Value (so the user might end up reading the
                        // stale Value contents).
                        @Suppress("unused") val newchm = _newchm // VOLATILE READ before returning V
                        return V
                    }
                    // Key hit - but slot is (possibly partially) copied to the new table.
                    // Finish the copy & retry in the new table.
                    return copy_slot_and_check(idx, key).get_impl(key) // Retry in the new table
                }
                // get and put must have the same key lookup logic!  But only 'put'
                // needs to force a table-resize for a too-long key-reprobe sequence.
                // Check for too-many-reprobes on get.
                if (++reprobe_cnt >= reprobe_limit(len))  // too many probes
                    return if (_newchm == null // Table copy in progress?
                    )
                        null // Nope!  A clear miss
                    else
                        copy_slot_and_check(idx, key).get_impl(key) // Retry in the new table


                idx = (idx + 1) and (len - 1) // Reprobe by 1!  (could now prefetch)
            }
        }

        // --- putIfMatch ---------------------------------------------------------
        // Put, Remove, PutIfAbsent, etc.  Return the old value.  If the returned
        // value is equal to expVal (or expVal is NO_MATCH_OLD) then the put can
        // be assumed to work (although might have been immediately overwritten).
        // Only the path through copy_slot passes in an expected value of null,
        // and putIfMatch only returns a null if passed in an expected null.
        fun putIfMatch(key: Long, putval: Any, expVal: Any?): Any? {
            val hash = hash(key)
            checkNotNull(putval)
            assert(putval !is Prime)
            assert(expVal !is Prime)
            val len = _keys.size
            var idx = (hash and (len - 1)) // The first key

            // ---
            // Key-Claim stanza: spin till we can claim a Key (or force a resizing).
            var reprobe_cnt = 0
            var K: Long
            var V: Any?
            while (true) {           // Spin till we get a Key slot
                V = _vals[idx] // Get old value
                K = _keys[idx] // Get current key
                if (K == NO_KEY) {     // Slot is free?
                    // Found an empty Key slot - which means this Key has never been in
                    // this table.  No need to put a Tombstone - the Key is not here!
                    if (putval === TOMBSTONE) return TOMBSTONE // Not-now & never-been in this table

                    if (expVal === MATCH_ANY) return TOMBSTONE // Will not match, even after K inserts

                    // Claim the zero key-slot
                    if (CAS_key(idx, NO_KEY, key)) { // Claim slot for Key
                        _slots.add(1) // Raise key-slots-used count
                        break // Got it!
                    }
                    // CAS to claim the key-slot failed.
                    //
                    // This re-read of the Key points out an annoying short-coming of Java
                    // CAS.  Most hardware CAS's report back the existing value - so that
                    // if you fail you have a *witness* - the value which caused the CAS
                    // to fail.  The Java API turns this into a boolean destroying the
                    // witness.  Re-reading does not recover the witness because another
                    // thread can write over the memory after the CAS.  Hence we can be in
                    // the unfortunate situation of having a CAS fail *for cause* but
                    // having that cause removed by a later store.  This turns a
                    // non-spurious-failure CAS (such as Azul has) into one that can
                    // apparently spuriously fail - and we avoid apparent spurious failure
                    // by not allowing Keys to ever change.
                    K = _keys[idx] // CAS failed, get updated value
                    assert(
                        K != NO_KEY // If keys[idx] is NO_KEY, CAS shoulda worked
                    )
                }
                // Key slot was not null, there exists a Key here
                if (K == key) break // Got it!


                // get and put must have the same key lookup logic!  Lest 'get' give
                // up looking too soon.
                //topmap._reprobes.add(1);
                if (++reprobe_cnt >= reprobe_limit(len)) {
                    // We simply must have a new table to do a 'put'.  At this point a
                    // 'get' will also go to the new table (if any).  We do not need
                    // to claim a key slot (indeed, we cannot find a free one to claim!).
                    val newchm = resize()
                    if (expVal != null) _nbhml.help_copy() // help along an existing copy

                    return newchm!!.putIfMatch(key, putval, expVal)
                }

                idx = (idx + 1) and (len - 1) // Reprobe!
            } // End of spinning till we get a Key slot


            while (true) {              // Spin till we insert a value
                // ---
                // Found the proper Key slot, now update the matching Value slot.  We
                // never put a null, so Value slots monotonically move from null to
                // not-null (deleted Values use Tombstone).  Thus if 'V' is null we
                // fail this fast cutout and fall into the check for table-full.
                if (putval === V) return V // Fast cutout for no-change


                // See if we want to move to a new table (to avoid high average re-probe
                // counts).  We only check on the initial set of a Value from null to
                // not-null (i.e., once per key-insert).
                if ((V == null && tableFull(
                        reprobe_cnt,
                        len
                    )) ||  // Or we found a Prime: resize is already in progress.  The resize
                    // call below will do a CAS on _newchm forcing the read.
                    V is Prime
                ) {
                    resize() // Force the new table copy to start
                    return copy_slot_and_check(idx, expVal).putIfMatch(key, putval, expVal)
                }

                // ---
                // We are finally prepared to update the existing table
                //assert !(V instanceof Prime); // always true, so IDE warnings if uncommented

                // Must match old, and we do not?  Then bail out now.  Note that either V
                // or expVal might be TOMBSTONE.  Also V can be null, if we've never
                // inserted a value before.  expVal can be null if we are called from
                // copy_slot.
                if (expVal !== NO_MATCH_OLD &&  // Do we care about expected-Value at all?
                    V !== expVal &&  // No instant match already?
                    (expVal !== MATCH_ANY || V === TOMBSTONE || V == null) && !(V == null && expVal === TOMBSTONE) &&  // Match on null/TOMBSTONE combo
                    (expVal == null || expVal != V)
                )  // Expensive equals check at the last
                    return V ?: TOMBSTONE // Do not update!


                // Actually change the Value in the Key,Value pair
                if (CAS_val(idx, V, putval)) break

                // CAS failed
                // Because we have no witness, we do not know why it failed.
                // Indeed, by the time we look again the value under test might have flipped
                // a thousand times and now be the expected value (despite the CAS failing).
                // Check for the never-succeed condition of a Prime value and jump to any
                // nested table, or else just re-run.

                // We would not need this load at all if CAS returned the value on which
                // the CAS failed (AKA witness). The new CAS semantics are supported via
                // VarHandle in JDK9.
                V = _vals[idx] // Get new value

                // If a Prime'd value got installed, we need to re-run the put on the
                // new table.  Otherwise we lost the CAS to another racing put.
                // Simply retry from the start.
                if (V is Prime) return copy_slot_and_check(idx, expVal).putIfMatch(key, putval, expVal)

                // Simply retry from the start.
                // NOTE: need the fence, since otherwise '_vals[idx]' load could be hoisted
                // out of loop.
                VarHandle.fullFence()
            }

            // CAS succeeded - we did the update!
            // Both normal put's and table-copy calls putIfMatch, but table-copy
            // does not (effectively) increase the number of live k/v pairs.
            if (expVal != null) {
                // Adjust sizes - a striped counter
                if ((V == null || V === TOMBSTONE) && putval !== TOMBSTONE) _size.add(1)
                if (!(V == null || V === TOMBSTONE) && putval === TOMBSTONE) _size.add(-1)
            }

            // We won; we know the update happened as expected.
            return if (V == null && expVal != null) TOMBSTONE else V
        }

        // --- tableFull ---------------------------------------------------------
        // Heuristic to decide if this table is too full, and we should start a
        // new table.  Note that if a 'get' call has reprobed too many times and
        // decided the table must be full, then always the estimate_sum must be
        // high and we must report the table is full.  If we do not, then we might
        // end up deciding that the table is not full and inserting into the
        // current table, while a 'get' has decided the same key cannot be in this
        // table because of too many reprobes.  The invariant is:
        //   slots.estimate_sum >= max_reprobe_cnt >= reprobe_limit(len)
        fun tableFull(reprobe_cnt: Int, len: Int): Boolean {
            return  // Do the cheap check first: we allow some number of reprobes always
            reprobe_cnt >= REPROBE_LIMIT &&
                    (reprobe_cnt >= reprobe_limit(len) ||  // More expensive check: see if the table is > 1/2 full.
                            _slots.estimate_get() >= (len shr 1))
        }

        // --- resize ------------------------------------------------------------
        // Resizing after too many probes.  "How Big???" heuristics are here.
        // Callers will (not this routine) will 'help_copy' any in-progress copy.
        // Since this routine has a fast cutout for copy-already-started, callers
        // MUST 'help_copy' lest we have a path which forever runs through
        // 'resize' only to discover a copy-in-progress which never progresses.
        fun resize(): CHM? {
            // Check for resize already in progress, probably triggered by another thread
            var newchm = _newchm // VOLATILE READ
            if (newchm != null)  // See if resize is already in progress
                return newchm // Use the new table already


            // No copy in-progress, so start one.  First up: compute new table size.
            val oldlen = _keys.size // Old count of K,V pairs allowed
            val sz = size() // Get current table count of active K,V pairs
            var newsz = sz // First size estimate

            // Heuristic to determine new size.  We expect plenty of dead-slots-with-keys
            // and we need some decent padding to avoid endless reprobing.
            if (_nbhml._opt_for_space) {
                // This heuristic leads to a much denser table with a higher reprobe rate
                if (sz >= (oldlen shr 1))  // If we are >50% full of keys then...
                    newsz = oldlen shl 1 // Double size
            } else {
                if (sz >= (oldlen shr 2)) { // If we are >25% full of keys then...
                    newsz = oldlen shl 1 // Double size
                    if (sz >= (oldlen shr 1))  // If we are >50% full of keys then...
                        newsz = oldlen shl 2 // Double double size
                }
            }

            // Last (re)size operation was very recent?  Then double again
            // despite having few live keys; slows down resize operations
            // for tables subject to a high key churn rate - but do not
            // forever grow the table.  If there is a high key churn rate
            // the table needs a steady state of rare same-size resize
            // operations to clean out the dead keys.
            val tm = System.currentTimeMillis()
            if (newsz <= oldlen &&  // New table would shrink or hold steady?
                tm <= _nbhml._last_resize_milli + 10000
            )  // Recent resize (less than 10 sec ago)
                newsz = oldlen shl 1 // Double the existing size


            // Do not shrink, ever.  If we hit this size once, assume we
            // will again.
            if (newsz < oldlen) newsz = oldlen

            // Convert to power-of-2
            var log2: Int
            log2 = MIN_SIZE_LOG
            while ((1 shl log2) < newsz) {
                // Compute log2 of size
                log2++
            }
            var len = ((1L shl log2) shl 1) + 2
            // prevent integer overflow - limit of 2^31 elements in a Java array
            // so here, 2^30 + 2 is the largest number of elements in the hash table
            if (len.toInt().toLong() != len) {
                log2 = 30
                len = (1L shl log2) + 2
                if (sz > ((len shr 2) + (len shr 1))) throw RuntimeException("Table is full.")
            }

            // Now limit the number of threads actually allocating memory to a
            // handful - lest we have 750 threads all trying to allocate a giant
            // resized array.
            var r = _resizers
            while (!_resizerUpdater.compareAndSet(
                    this, r, r + 1
                )
            ) r = _resizers
            // Size calculation: 2 words (K+V) per table entry, plus a handful.  We
            // guess at 64-bit pointers; 32-bit pointers screws up the size calc by
            // 2x but does not screw up the heuristic very much.
            val megs = ((((1L shl log2) shl 1) + 8) shl 3 /*word to bytes*/) shr 20 /*megs*/
            if (r >= 2 && megs > 0) { // Already 2 guys trying; wait and see
                newchm = _newchm // Between dorking around, another thread did it
                if (newchm != null)  // See if resize is already in progress
                    return newchm // Use the new table already

                // We could use a wait with timeout, so we'll wakeup as soon as the new table
                // is ready, or after the timeout in any case.
                //synchronized( this ) { wait(8*megs); }         // Timeout - we always wakeup
                // For now, sleep a tad and see if the 2 guys already trying to make
                // the table actually get around to making it happen.
                try {
                    Thread.sleep(megs)
                } catch (e: Exception) { /*empty*/
                }
            }
            // Last check, since the 'new' below is expensive and there is a chance
            // that another thread slipped in a new thread while we ran the heuristic.
            newchm = _newchm
            if (newchm != null)  // See if resize is already in progress
                return newchm // Use the new table already


            // New CHM - actually allocate the big arrays
            newchm = CHM(_nbhml, _size, log2)

            // Another check after the slow allocation
            if (_newchm != null)  // See if resize is already in progress
                return _newchm // Use the new table already


            // The new table must be CAS'd in so only 1 winner amongst duplicate
            // racing resizing threads.  Extra CHM's will be GC'd.
            if (CAS_newchm(newchm)) { // NOW a resize-is-in-progress!
                //notifyAll();            // Wake up any sleepers
                //long nano = System.nanoTime();
                //System.out.println(" "+nano+" Resize from "+oldlen+" to "+(1<<log2)+" and had "+(_resizers-1)+" extras" );
                //System.out.print("["+log2);
            } else  // CAS failed?
                newchm = _newchm // Reread new table

            return newchm
        }

        // The next part of the table to copy.  It monotonically transits from zero
        // to _keys.length.  Visitors to the table can claim 'work chunks' by
        // CAS'ing this field up, then copying the indicated indices from the old
        // table to the new table.  Workers are not required to finish any chunk;
        // the counter simply wraps and work is copied duplicately until somebody
        // somewhere completes the count.
        @Volatile
        var _copyIdx: Long = 0

        // Work-done reporting.  Used to efficiently signal when we can move to
        // the new table.  From 0 to len(oldkvs) refers to copying from the old
        // table to the new.
        @Volatile
        var _copyDone: Long = 0

        // Simple constructor
        init {
            _size = size
            _slots = ConcurrentAutoLongTable()
            _keys = LongArray(1 shl logsize)
            _vals = arrayOfNulls(1 shl logsize)
        }

        // --- help_copy_impl ----------------------------------------------------
        // Help along an existing resize operation.  We hope its the top-level
        // copy (it was when we started) but this CHM might have been promoted out
        // of the top position.
        fun help_copy_impl(copy_all: Boolean) {
            val newchm = checkNotNull(_newchm)
            val oldlen = _keys.size // Total amount to copy
            val MIN_COPY_WORK = min(oldlen.toDouble(), 1024.0).toInt() // Limit per-thread work

            // ---
            var panic_start = -1
            var copyidx = -9999 // Fool javac to think it's initialized
            while (_copyDone < oldlen) { // Still needing to copy?
                // Carve out a chunk of work.  The counter wraps around so every
                // thread eventually tries to copy every slot repeatedly.

                // We "panic" if we have tried TWICE to copy every slot - and it still
                // has not happened.  i.e., twice some thread somewhere claimed they
                // would copy 'slot X' (by bumping _copyIdx) but they never claimed to
                // have finished (by bumping _copyDone).  Our choices become limited:
                // we can wait for the work-claimers to finish (and become a blocking
                // algorithm) or do the copy work ourselves.  Tiny tables with huge
                // thread counts trying to copy the table often 'panic'.

                if (panic_start == -1) { // No panic?
                    copyidx = _copyIdx.toInt()
                    while (copyidx < (oldlen shl 1) &&  // 'panic' check
                        !_copyIdxUpdater.compareAndSet(
                            this, copyidx.toLong(), (copyidx + MIN_COPY_WORK).toLong()
                        )
                    ) copyidx = _copyIdx.toInt() // Re-read

                    if (copyidx >= (oldlen shl 1))  // Panic!
                        panic_start = copyidx // Record where we started to panic-copy
                }

                // We now know what to copy.  Try to copy.
                var workdone = 0
                for (i in 0..<MIN_COPY_WORK) if (copy_slot((copyidx + i) and (oldlen - 1)))  // Made an oldtable slot go dead?
                    workdone++ // Yes!

                if (workdone > 0)  // Report work-done occasionally
                    copy_check_and_promote(workdone) // See if we can promote


                //for( int i=0; i<MIN_COPY_WORK; i++ )
                //  if( copy_slot((copyidx+i)&(oldlen-1)) ) // Made an oldtable slot go dead?
                //    copy_check_and_promote( 1 );// See if we can promote
                copyidx += MIN_COPY_WORK
                // Uncomment these next 2 lines to turn on incremental table-copy.
                // Otherwise this thread continues to copy until it is all done.
                if (!copy_all && panic_start == -1)  // No panic?
                    return  // Then done copying after doing MIN_COPY_WORK
            }
            // Extra promotion check, in case another thread finished all copying
            // then got stalled before promoting.
            copy_check_and_promote(0) // See if we can promote
        }

        // --- copy_slot_and_check -----------------------------------------------
        // Copy slot 'idx' from the old table to the new table.  If this thread
        // confirmed the copy, update the counters and check for promotion.
        //
        // Returns the result of reading the volatile _newchm, mostly as a
        // convenience to callers.  We come here with 1-shot copy requests
        // typically because the caller has found a Prime, and has not yet read
        // the _newchm volatile - which must have changed from null-to-not-null
        // before any Prime appears.  So the caller needs to read the _newchm
        // field to retry his operation in the new table, but probably has not
        // read it yet.
        fun copy_slot_and_check(idx: Int, should_help: Any?): CHM {
            // We're only here because the caller saw a Prime, which implies a
            // table-copy is in progress.
            checkNotNull(_newchm)
            if (copy_slot(idx))  // Copy the desired slot
                copy_check_and_promote(1) // Record the slot copied

            // Generically help along any copy (except if called recursively from a helper)
            if (should_help != null) _nbhml.help_copy()
            return _newchm
        }

        // --- copy_check_and_promote --------------------------------------------
        fun copy_check_and_promote(workdone: Int) {
            val oldlen = _keys.size
            // We made a slot unusable and so did some of the needed copy work
            var copyDone = _copyDone
            var nowDone = copyDone + workdone
            assert(nowDone <= oldlen)
            if (workdone > 0) {
                while (!_copyDoneUpdater.compareAndSet(
                        this, copyDone, nowDone
                    )
                ) {
                    copyDone = _copyDone // Reload, retry
                    nowDone = copyDone + workdone
                    assert(nowDone <= oldlen)
                }
            }

            // Check for copy being ALL done, and promote.  Note that we might have
            // nested in-progress copies and manage to finish a nested copy before
            // finishing the top-level copy.  We only promote top-level copies.
            if (nowDone == oldlen.toLong() &&  // Ready to promote this table?
                _nbhml._chm == this &&  // Looking at the top-level table?
                // Attempt to promote
                _nbhml.CAS(_chm_handler!!, this, _newchm!!)
            ) {
                _nbhml._last_resize_milli = System.currentTimeMillis() // Record resize time for next check
            }
        }

        // --- copy_slot ---------------------------------------------------------
        // Copy one K/V pair from oldkvs[i] to newkvs.  Returns true if we can
        // confirm that we set an old-table slot to TOMBPRIME, and only returns after
        // updating the new table.  We need an accurate confirmed-copy count so
        // that we know when we can promote (if we promote the new table too soon,
        // other threads may 'miss' on values not-yet-copied from the old table).
        // We don't allow any direct updates on the new table, unless they first
        // happened to the old table - so that any transition in the new table from
        // null to not-null must have been from a copy_slot (or other old-table
        // overwrite) and not from a thread directly writing in the new table.
        fun copy_slot(idx: Int): Boolean {
            // Blindly set the key slot from NO_KEY to some key which hashes here,
            // to eagerly stop fresh put's from inserting new values in the old
            // table when the old table is mid-resize.  We don't need to act on the
            // results here, because our correctness stems from box'ing the Value
            // field.  Slamming the Key field is a minor speed optimization.
            var key: Long
            while ((_keys[idx].also { key = it }) == NO_KEY) CAS_key(
                idx,
                NO_KEY,
                (idx + _keys.size).toLong() /*a non-zero key which hashes here*/
            )

            // ---
            // Prevent new values from appearing in the old table.
            // Box what we see in the old table, to prevent further updates.
            var oldval = _vals[idx]!! // Read OLD table
            while (oldval !is Prime) {
                val box = if (oldval == null || oldval === TOMBSTONE) TOMBPRIME else Prime(oldval)
                if (CAS_val(idx, oldval, box)) { // CAS down a box'd version of oldval
                    // If we made the Value slot hold a TOMBPRIME, then we both
                    // prevented further updates here but also the (absent) oldval is
                    // vaccuously available in the new table.  We return with true here:
                    // any thread looking for a value for this key can correctly go
                    // straight to the new table and skip looking in the old table.
                    if (box == TOMBPRIME) return true
                    // Otherwise we boxed something, but it still needs to be
                    // copied into the new table.
                    oldval = box // Record updated oldval
                    break // Break loop; oldval is now boxed by us
                }
                oldval = _vals[idx]!! // Else try, try again
            }
            if (oldval === TOMBPRIME) return false // Copy already complete here!


            // ---
            // Copy the value into the new table, but only if we overwrite a null.
            // If another value is already in the new table, then somebody else
            // wrote something there and that write is happens-after any value that
            // appears in the old table.
            val old_unboxed = (oldval as Prime)._V
            assert(old_unboxed !== TOMBSTONE)
            val copied_into_new = (_newchm!!.putIfMatch(key, old_unboxed, null) == null)

            // ---
            // Finally, now that any old value is exposed in the new table, we can
            // forever hide the old-table value by slapping a TOMBPRIME down.  This
            // will stop other threads from uselessly attempting to copy this slot
            // (i.e., it's a speed optimization not a correctness issue).
            while (oldval !== TOMBPRIME && !CAS_val(idx, oldval, TOMBPRIME)) oldval =
                _vals[idx]!!

            return copied_into_new
        } // end copy_slot

        companion object {
            private val _newchmUpdater: AtomicReferenceFieldUpdater<CHM, CHM?> = AtomicReferenceFieldUpdater.newUpdater(
                CHM::class.java,
                CHM::class.java, "_newchm"
            )

            private val _resizerUpdater: AtomicLongFieldUpdater<CHM> = AtomicLongFieldUpdater.newUpdater(
                CHM::class.java, "_resizers"
            )

            private val _copyIdxUpdater: AtomicLongFieldUpdater<CHM> = AtomicLongFieldUpdater.newUpdater(
                CHM::class.java, "_copyIdx"
            )

            private val _copyDoneUpdater: AtomicLongFieldUpdater<CHM> = AtomicLongFieldUpdater.newUpdater(
                CHM::class.java, "_copyDone"
            )
        }
    } // End of CHM


    // --- Snapshot ------------------------------------------------------------
    // The main class for iterating over the NBHM.  It "snapshots" a clean
    // view of the K/V array.
    private inner class SnapshotV : MutableIterator<TypeV?>, Enumeration<TypeV?> {
        val _sschm: CHM

        fun length(): Int {
            return _sschm._keys.size
        }

        fun key(idx: Int): Long {
            return _sschm._keys[idx]
        }

        private var _idx: Int // -2 for NO_KEY, -1 for CHECK_NEW_TABLE_LONG, 0-keys.length
        private var _nextK: Long = 0
        private var _prevK: Long = 0 // Last 2 keys found
        private var _nextV: TypeV? = null
        private var _prevV: TypeV? = null // Last 2 values found

        init {
            var topchm: CHM?
            while (true) {           // Verify no table-copy-in-progress
                topchm = _chm
                if (topchm!!._newchm == null)  // No table-copy-in-progress
                    break
                // Table copy in-progress - so we cannot get a clean iteration.  We
                // must help finish the table copy before we can start iterating.
                topchm.help_copy_impl(true)
            }
            // The "linearization point" for the iteration.  Every key in this table
            // will be visited, but keys added later might be skipped or even be
            // added to a following table (also not iterated over).
            _sschm = topchm!!
            // Warm-up the iterator
            _idx = -1
            next()
        }

        override fun hasNext(): Boolean {
            return _nextV != null
        }

        override fun next(): TypeV? {
            // 'next' actually knows what the next value will be - it had to
            // figure that out last go 'round lest 'hasNext' report true and
            // some other thread deleted the last value.  Instead, 'next'
            // spends all its effort finding the key that comes after the
            // 'next' key.
            if (_idx != -1 && _nextV == null) throw NoSuchElementException()
            _prevK = _nextK // This will become the previous key
            _prevV = _nextV // This will become the previous value
            _nextV = null // We have no more next-key
            // Attempt to set <_nextK,_nextV> to the next K,V pair.
            // _nextV is the trigger: stop searching when it is != null
            if (_idx == -1) {        // Check for NO_KEY
                _idx = 0 // Setup for next phase of search
                _nextK = NO_KEY
                if ((get(_nextK).also { _nextV = it }) != null) return _prevV
            }
            while (_idx < length()) {  // Scan array
                _nextK = key(_idx++) // Get a key that definitely is in the set (for the moment!)
                if (_nextK != NO_KEY &&  // Found something?
                    (get(_nextK).also { _nextV = it }) != null
                ) break // Got it!  _nextK is a valid Key
            } // Else keep scanning

            return _prevV // Return current value.
        }

        fun removeKey() {
            checkNotNull(_prevV)
            this@Long2ObjectNonBlockingMap.putIfMatch(_prevK, TOMBSTONE, NO_MATCH_OLD)
            _prevV = null
        }

        override fun remove() {
            // NOTE: it would seem logical that value removal will semantically mean
            // removing the matching value for the mapping <k,v>, but the JDK always
            // removes by key, even when the value has changed.
            removeKey()
        }

        override fun nextElement(): TypeV? {
            return next()
        }

        override fun hasMoreElements(): Boolean {
            return hasNext()
        }
    }

    /**
     * Returns an enumeration of the values in this table.
     *
     * @return an enumeration of the values in this table
     * @see .values
     */
    fun elements(): Enumeration<TypeV> {
        return SnapshotV()
    }

    // --- values --------------------------------------------------------------
    /**
     * Returns a [Collection] view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are reflected
     * in the collection, and vice-versa.  The collection supports element
     * removal, which removes the corresponding mapping from this map, via the
     * <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt> operations.
     * It does not support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     *
     * The view's <tt>iterator</tt> is a "weakly consistent" iterator that
     * will never throw [ConcurrentModificationException], and guarantees
     * to traverse elements as they existed upon construction of the iterator,
     * and may (but is not guaranteed to) reflect any modifications subsequent
     * to construction.
     */
    override fun values(): Collection<TypeV> {
        return object : AbstractCollection<TypeV?>() {
            override fun clear() {
                this@Long2ObjectNonBlockingMap.clear()
            }

            override fun size(): Int {
                return this@Long2ObjectNonBlockingMap.size
            }

            override fun contains(v: Any?): Boolean {
                return this@Long2ObjectNonBlockingMap.containsValue(v)
            }

            override fun iterator(): MutableIterator<TypeV?> {
                return SnapshotV()
            }
        }
    }

    // --- keySet --------------------------------------------------------------
    /**
     * A class which implements the [Iterator] and [Enumeration]
     * interfaces, generified to the [Long] class and supporting a
     * **non-auto-boxing** [.nextLong] function.
     */
    inner class IteratorLong : MutableIterator<Long>, Enumeration<Long>, LongIterator {
        private val _ss: SnapshotV

        /**
         * A new IteratorLong
         */
        init {
            _ss = SnapshotV()
        }

        /**
         * Remove last key returned by [.next] or [.nextLong].
         */
        override fun remove() {
            _ss.removeKey()
        }

        /**
         * **Auto-box** and return the next key.
         */
        override fun next(): Long {
            _ss.next()
            return _ss._prevK
        }

        /**
         * Return the next key as a primitive `long`.
         */
        override fun nextLong(): Long {
            _ss.next()
            return _ss._prevK
        }

        /**
         * True if there are more keys to iterate over.
         */
        override fun hasNext(): Boolean {
            return _ss.hasNext()
        }

        /**
         * **Auto-box** and return the next key.
         */
        override fun nextElement(): Long {
            return next()
        }

        /**
         * True if there are more keys to iterate over.
         */
        override fun hasMoreElements(): Boolean {
            return hasNext()
        }
    }

    /**
     * Returns an enumeration of the **auto-boxed** keys in this table.
     * **Warning:** this version will auto-box all returned keys.
     *
     * @return an enumeration of the auto-boxed keys in this table
     * @see .keySet
     */
    fun keys(): Enumeration<Long> {
        return IteratorLong()
    }

    fun fastKeyIterator(): IteratorLong {
        return IteratorLong()
    }

    /**
     * Returns a [Set] view of the keys contained in this map; with care
     * the keys may be iterated over **without auto-boxing**.  The
     * set is backed by the map, so changes to the map are reflected in the
     * set, and vice-versa.  The set supports element removal, which removes
     * the corresponding mapping from this map, via the
     * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt>, and <tt>clear</tt> operations.  It does not support
     * the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     *
     * The view's <tt>iterator</tt> is a "weakly consistent" iterator that
     * will never throw [ConcurrentModificationException], and guarantees
     * to traverse elements as they existed upon construction of the iterator,
     * and may (but is not guaranteed to) reflect any modifications subsequent
     * to construction.
     */
    override fun keySet(): LongSet {
        return object : AbstractLongSet() {
            override fun iterator(): LongIterator {
                return IteratorLong()
            }

            override fun clear() {
                this@Long2ObjectNonBlockingMap.clear()
            }

            override fun size(): Int {
                return this@Long2ObjectNonBlockingMap.size
            }

            override fun contains(k: Long): Boolean {
                return this@Long2ObjectNonBlockingMap.containsKey(k)
            }

            override fun remove(k: Long): Boolean {
                return this@Long2ObjectNonBlockingMap.remove(k) != null
            }
        }
    }

    /**
     * Keys as a long array.  Array may be zero-padded if keys are concurrently deleted.
     */
    fun keySetLong(): LongArray {
        val dom = LongArray(size)
        val i: IteratorLong = keys.iterator() as IteratorLong
        var j = 0
        while (j < dom.size && i.hasNext()) dom[j++] = i.nextLong()
        return dom
    }

    // --- entrySet ------------------------------------------------------------
    // Warning: Each call to 'next' in this iterator constructs a new Long and a
    // new NBHMLEntry.
    private inner class NBHMLEntry(k: Long, v: TypeV) : LongObjectEntry<TypeV>,
        MutableMap.MutableEntry<Long?, TypeV> {
        override val longKey: Long = k
        override var value: TypeV
            private set

        init {
            this.value = v
        }

        override fun setValue(`val`: TypeV): TypeV {
            if (`val` == null) throw NullPointerException()
            value = `val`
            return put(longKey, `val`)
        }
    }

    private inner class SnapshotE : MutableIterator<LongObjectEntry<TypeV>> {
        val _ss: SnapshotV

        init {
            _ss = SnapshotV()
        }

        override fun remove() {
            // NOTE: it would seem logical that entry removal will semantically mean
            // removing the matching pair <k,v>, but the JDK always removes by key,
            // even when the value has changed.
            _ss.removeKey()
        }

        override fun next(): LongObjectEntry<TypeV> {
            _ss.next()
            return NBHMLEntry(_ss._prevK, _ss._prevV)
        }

        override fun hasNext(): Boolean {
            return _ss.hasNext()
        }
    }

    private inner class SnapshotESlow :
        MutableIterator<Map.Entry<Long, TypeV>> {
        val _ss: SnapshotV

        init {
            _ss = SnapshotV()
        }

        override fun remove() {
            // NOTE: it would seem logical that entry removal will semantically mean
            // removing the matching pair <k,v>, but the JDK always removes by key,
            // even when the value has changed.
            _ss.removeKey()
        }

        override fun next(): Map.Entry<Long, TypeV> {
            _ss.next()
            return NBHMLEntry(_ss._prevK, _ss._prevV)
        }

        override fun hasNext(): Boolean {
            return _ss.hasNext()
        }
    }

    /**
     * Returns a [Set] view of the mappings contained in this map.  The
     * set is backed by the map, so changes to the map are reflected in the
     * set, and vice-versa.  The set supports element removal, which removes
     * the corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt>, and <tt>clear</tt> operations.  It does not support
     * the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     *
     * The view's <tt>iterator</tt> is a "weakly consistent" iterator
     * that will never throw [ConcurrentModificationException],
     * and guarantees to traverse elements as they existed upon
     * construction of the iterator, and may (but is not guaranteed to)
     * reflect any modifications subsequent to construction.
     *
     *
     * **Warning:** the iterator associated with this Set
     * requires the creation of [Entry] objects with each
     * iteration.  The [cn.nukkit.utils.collection.nb.Long2ObjectNonBlockingMap]
     * does not normally create or using [Entry] objects so
     * they will be created soley to support this iteration.  Iterating using
     * [Map.keySet] or [Map.values] will be more efficient.  In addition,
     * this version requires **auto-boxing** the keys.
     */
    override fun entrySet(): Set<Map.Entry<Long, TypeV>> {
        return object : AbstractSet<Map.Entry<Long?, TypeV>?>() {
            override fun clear() {
                this@Long2ObjectNonBlockingMap.clear()
            }

            override fun size(): Int {
                return this@Long2ObjectNonBlockingMap.size
            }

            override fun remove(o: Any): Boolean {
                if (o !is Map.Entry<*, *>) return false
                return this@Long2ObjectNonBlockingMap.remove(o.key, o.value)
            }

            override fun contains(o: Any): Boolean {
                if (o !is Map.Entry<*, *>) return false
                val v = get(o.key)
                return v != null && v == o.value
            }

            override fun iterator(): MutableIterator<Map.Entry<Long, TypeV>> {
                return SnapshotESlow()
            }
        }
    }

    fun fastEntrySet(): Set<LongObjectEntry<TypeV>> {
        return object : AbstractSet<LongObjectEntry<TypeV>?>() {
            override fun clear() {
                this@Long2ObjectNonBlockingMap.clear()
            }

            override fun size(): Int {
                return this@Long2ObjectNonBlockingMap.size
            }

            override fun remove(o: Any): Boolean {
                if (o !is Map.Entry<*, *>) return false
                return this@Long2ObjectNonBlockingMap.remove(o.key, o.value)
            }

            override fun contains(o: Any): Boolean {
                if (o !is Map.Entry<*, *>) return false
                val v = get(o.key)
                return v != null && v == o.value
            }

            override fun iterator(): MutableIterator<LongObjectEntry<TypeV>> {
                return SnapshotE()
            }
        }
    }

    // --- writeObject -------------------------------------------------------
    // Write a NBHML to a stream
    @Serial
    @Throws(IOException::class)
    private fun writeObject(s: ObjectOutputStream) {
        s.defaultWriteObject() // Write nothing
        for (K in keys) {
            val V: Any? = get(K) // Do an official 'get'
            s.writeLong(K) // Write the <long,TypeV> pair
            s.writeObject(V)
        }
        s.writeLong(NO_KEY) // Sentinel to indicate end-of-data
        s.writeObject(null)
    }

    // --- readObject --------------------------------------------------------
    // Read a NBHML from a stream
    @Serial
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(s: ObjectInputStream) {
        s.defaultReadObject() // Read nothing
        initialize(MIN_SIZE)
        while (true) {
            val K = s.readLong()
            val V: TypeV? = s.readObject() as TypeV
            if (K == NO_KEY && V == null) break
            put(K, V) // Insert with an offical put
        }
    }

    /**
     * Creates a shallow copy of this hashtable. All the structure of the
     * hashtable itself is copied, but the keys and values are not cloned.
     * This is a relatively expensive operation.
     *
     * @return a clone of the hashtable.
     */
    public override fun clone(): Long2ObjectNonBlockingMap<TypeV> {
        try {
            // Must clone, to get the class right; NBHML might have been
            // extended so it would be wrong to just make a new NBHML.
            val t = super.clone() as Long2ObjectNonBlockingMap<TypeV>
            // But I don't have an atomic clone operation - the underlying _kvs
            // structure is undergoing rapid change.  If I just clone the _kvs
            // field, the CHM in _kvs[0] won't be in sync.
            //
            // Wipe out the cloned array (it was shallow anyways).
            t.clear()
            // Now copy sanely
            for (K in keySetLong()) t[K] = get(K)
            return t
        } catch (e: CloneNotSupportedException) {
            // this shouldn't happen, since we are Cloneable
            throw InternalError()
        }
    }

    /**
     * It is not possible to atomically compute a value in a ConcurrentMap without locks.
     */
    @Deprecated("")
    override fun computeIfAbsent(key: Long, mappingFunction: Function<in Long, out TypeV>): TypeV? {
        return super<ConcurrentMap>.computeIfAbsent(key, mappingFunction)
    }

    /**
     * It is not possible to atomically compute a value in a ConcurrentMap without locks.
     */
    @Deprecated("")
    override fun computeIfPresent(key: Long, remappingFunction: BiFunction<in Long, in TypeV, out TypeV?>): TypeV? {
        return super<ConcurrentMap>.computeIfPresent(key, remappingFunction)
    }

    /**
     * It is not possible to atomically compute a value in a ConcurrentMap without locks.
     */
    @Deprecated("")
    override fun compute(key: Long, remappingFunction: BiFunction<in Long, in TypeV?, out TypeV?>): TypeV? {
        return super<ConcurrentMap>.compute(key, remappingFunction)
    }

    /**
     * It is not possible to atomically compute a value in a ConcurrentMap without locks.
     */
    @Deprecated("")
    override fun merge(key: Long, value: TypeV, remappingFunction: BiFunction<in TypeV, in TypeV, out TypeV?>): TypeV? {
        return super<ConcurrentMap>.merge(key, value, remappingFunction)
    }

    companion object {
        @Serial
        private val serialVersionUID = -6451277160490981997L

        private const val REPROBE_LIMIT = 10 // Too many reprobes then force a table-resize

        // --- Bits to allow Unsafe access to arrays
        private val _OHandler: VarHandle = MethodHandles.arrayElementVarHandle(Array<Any>::class.java)
        private val _LHandler: VarHandle = MethodHandles.arrayElementVarHandle(
            LongArray::class.java
        )

        // --- Bits to allow Unsafe CAS'ing of the CHM field
        private var _chm_handler: VarHandle? = null // fieldOffset(Long2ObjectNonBlockingMap.class, "_chm");
        private var _val_1_handler: VarHandle? = null // fieldOffset(Long2ObjectNonBlockingMap.class, "_val_1");

        init {
            try {
                _chm_handler = MethodHandles.lookup().`in`(
                    Long2ObjectNonBlockingMap::class.java
                )
                    .findVarHandle(Long2ObjectNonBlockingMap::class.java, "_chm", CHM::class.java)
                _val_1_handler = MethodHandles.lookup().`in`(
                    Long2ObjectNonBlockingMap::class.java
                )
                    .findVarHandle(Long2ObjectNonBlockingMap::class.java, "_val_1", Any::class.java)
            } catch (e: NoSuchFieldException) {
                throw RuntimeException(e) // Should never happen
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            }
        }

        // --- Minimum table size ----------------
        // Pick size 16 K/V pairs, which turns into (16*2)*4+12 = 140 bytes on a
        // standard 32-bit HotSpot, and (16*2)*8+12 = 268 bytes on 64-bit Azul.
        private const val MIN_SIZE_LOG = 4 //
        private const val MIN_SIZE = (1 shl MIN_SIZE_LOG) // Must be power of 2

        // --- Sentinels -------------------------
        // No-Match-Old - putIfMatch does updates only if it matches the old value,
        // and NO_MATCH_OLD basically counts as a wildcard match.
        private val NO_MATCH_OLD = Any() // Sentinel

        // Match-Any-not-null - putIfMatch does updates only if it find a real old
        // value.
        private val MATCH_ANY = Any() // Sentinel

        // This K/V pair has been deleted (but the Key slot is forever claimed).
        // The same Key can be reinserted with a new value later.
        private val TOMBSTONE = Any()

        // Prime'd or box'd version of TOMBSTONE.  This K/V pair was deleted, then a
        // table resize started.  The K/V pair has been marked so that no new
        // updates can happen to the old table (and since the K/V pair was deleted
        // nothing was copied to the new table).
        private val TOMBPRIME = Prime(TOMBSTONE)

        // I exclude 1 long from the 2^64 possibilities, and test for it before
        // entering the main array.  The NO_KEY value must be zero, the initial
        // value set by Java before it hands me the array.
        private const val NO_KEY = 0L

        // --- reprobe_limit -----------------------------------------------------
        // Heuristic to decide if we have reprobed toooo many times.  Running over
        // the reprobe limit on a 'get' call acts as a 'miss'; on a 'put' call it
        // can trigger a table resize.  Several places must have exact agreement on
        // what the reprobe_limit is, so we share it here.
        private fun reprobe_limit(len: Int): Int {
            return REPROBE_LIMIT + (len shr 4)
        }

        // --- hash ----------------------------------------------------------------
        // Helper function to spread lousy hashCodes Throws NPE for null Key, on
        // purpose - as the first place to conveniently toss the required NPE for a
        // null Key.
        private fun hash(h: Long): Int {
            var h = h
            h = h xor ((h ushr 20) xor (h ushr 12))
            h = h xor ((h ushr 7) xor (h ushr 4))
            h += h shl 7 // smear low bits up high, for hashcodes that only differ by 1
            return h.toInt()
        }
    }
} // End Long2ObjectNonBlockingMap class

