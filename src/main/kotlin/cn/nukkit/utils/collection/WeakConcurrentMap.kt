/*
 * Originally from Mockito project.
 * ======================================
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package cn.nukkit.utils.collection

import java.lang.ref.Reference
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 *
 *
 * A thread-safe map with weak keys. Entries are based on a key's system hash code and keys are considered
 * equal only by reference equality.
 *
 * This class does not implement the [java.util.Map] interface because this implementation is incompatible
 * with the map contract. While iterating over a map's entries, any key that has not passed iteration is referenced non-weakly.
 */
open class WeakConcurrentMap<K, V>(cleanerThread: Boolean) : ReferenceQueue<K>(), Runnable,
    Iterable<Map.Entry<K, V>?> {
    val target: ConcurrentHashMap<WeakKey<K>, V?>

    /**
     * @return The cleaner thread or `null` if no such thread was set.
     */
    var cleanerThread: Thread? = null

    /**
     * @param cleanerThread `true` if a thread should be started that removes stale entries.
     */
    init {
        target = ConcurrentHashMap()
        if (cleanerThread) {
            this.cleanerThread = Thread(this)
            cleanerThread.setName("weak-ref-cleaner-" + ID.getAndIncrement())
            cleanerThread.setPriority(Thread.MIN_PRIORITY)
            cleanerThread.setDaemon(true)
            cleanerThread.start()
        } else {
            this.cleanerThread = null
        }
    }

    /**
     * @param key The key of the entry.
     * @return The value of the entry or the default value if it did not exist.
     */
    open fun get(key: K?): V? {
        if (key == null) {
            throw NullPointerException()
        }
        var value = target[LatentKey<K>(key)]
        if (value == null) {
            value = defaultValue(key)
            if (value != null) {
                val previousValue = target.putIfAbsent(WeakKey(key, this), value)
                if (previousValue != null) {
                    value = previousValue
                }
            }
        }
        return value
    }

    /**
     * @param key The key of the entry.
     * @return `true` if the key already defines a value.
     */
    open fun containsKey(key: K?): Boolean {
        if (key == null) {
            throw NullPointerException()
        }
        return target.containsKey(LatentKey<K>(key) as Any)
    }

    /**
     * @param key   The key of the entry.
     * @param value The value of the entry.
     * @return The previous entry or `null` if it does not exist.
     */
    open fun put(key: K?, value: V?): V? {
        if (key == null || value == null) {
            throw NullPointerException()
        }
        return target.put(WeakKey(key, this), value)
    }

    /**
     * @param key The key of the entry.
     * @return The removed entry or `null` if it does not exist.
     */
    open fun remove(key: K?): V? {
        if (key == null) {
            throw NullPointerException()
        }
        return target.remove(LatentKey<K>(key) as Any)
    }

    /**
     * Clears the entire map.
     */
    fun clear() {
        target.clear()
    }

    /**
     * Creates a default value. There is no guarantee that the requested value will be set as a once it is created
     * in case that another thread requests a value for a key concurrently.
     *
     * @param key The key for which to create a default value.
     * @return The default value for a key without value or `null` for not defining a default value.
     */
    protected fun defaultValue(key: K): V? {
        return null
    }

    /**
     * Cleans all unused references.
     */
    fun expungeStaleEntries() {
        var reference: Reference<*>?
        while ((poll().also { reference = it }) != null) {
            target.remove(reference)
        }
    }

    /**
     * Returns the approximate size of this map where the returned number is at least as big as the actual number of entries.
     *
     * @return The minimum size of this map.
     */
    open fun approximateSize(): Int {
        return target.size
    }

    fun clearDeadReferences() {
        while (true) {
            val tmp = poll() ?: break
            target.remove(tmp)
        }
    }

    override fun run() {
        try {
            while (true) {
                target.remove(remove())
            }
        } catch (ignored: InterruptedException) {
            clear()
        }
    }

    override fun iterator(): MutableIterator<Map.Entry<K, V>> {
        return EntryIterator(target.entries.iterator())
    }

    /*
     * Why this works:
     * ---------------
     *
     * Note that this map only supports reference equality for keys and uses system hash codes. Also, for the
     * WeakKey instances to function correctly, we are voluntarily breaking the Java API contract for
     * hashCode/equals of these instances.
     *
     *
     * System hash codes are immutable and can therefore be computed prematurely and are stored explicitly
     * within the WeakKey instances. This way, we always know the correct hash code of a key and always
     * end up in the correct bucket of our target map. This remains true even after the weakly referenced
     * key is collected.
     *
     * If we are looking up the value of the current key via WeakConcurrentMap::get or any other public
     * API method, we know that any value associated with this key must still be in the map as the mere
     * existence of this key makes it ineligible for garbage collection. Therefore, looking up a value
     * using another WeakKey wrapper guarantees a correct result.
     *
     * If we are looking up the map entry of a WeakKey after polling it from the reference queue, we know
     * that the actual key was already collected and calling WeakKey::get returns null for both the polled
     * instance and the instance within the map. Since we explicitly stored the identity hash code for the
     * referenced value, it is however trivial to identify the correct bucket. From this bucket, the first
     * weak key with a null reference is removed. Due to hash collision, we do not know if this entry
     * represents the weak key. However, we do know that the reference queue polls at least as many weak
     * keys as there are stale map entries within the target map. If no key is ever removed from the map
     * explicitly, the reference queue eventually polls exactly as many weak keys as there are stale entries.
     *
     * Therefore, we can guarantee that there is no memory leak.
     */
    class WeakKey<T> internal constructor(key: T, queue: ReferenceQueue<in T>?) :
        WeakReference<T>(key, queue) {
        private val hashCode = System.identityHashCode(key)

        override fun hashCode(): Int {
            return hashCode
        }

        override fun equals(other: Any): Boolean {
            return if (other is LatentKey<*>) {
                other.key === get()
            } else {
                (other as WeakKey<*>).get() === get()
            }
        }
    }

    /*
     * A latent key must only be used for looking up instances within a map. For this to work, it implements an identical contract for
     * hash code and equals as the WeakKey implementation. At the same time, the latent key implementation does not extend WeakReference
     * and avoids the overhead that a weak reference implies.
     */
    private class LatentKey<T>(val key: T) {
        private val hashCode = System.identityHashCode(key)

        override fun equals(other: Any): Boolean {
            return if (other is LatentKey<*>) {
                other.key === key
            } else {
                (other as WeakKey<*>).get() === key
            }
        }

        override fun hashCode(): Int {
            return hashCode
        }
    }

    /**
     * A [WeakConcurrentMap] where stale entries are removed as a side effect of interacting with this map.
     */
    class WithInlinedExpunction<K, V> : WeakConcurrentMap<K, V>(false) {
        override fun get(key: K): V {
            expungeStaleEntries()
            return super.get(key)
        }

        override fun containsKey(key: K): Boolean {
            expungeStaleEntries()
            return super.containsKey(key)
        }

        override fun put(key: K, value: V): V {
            expungeStaleEntries()
            return super.put(key, value)
        }

        override fun remove(key: K): V {
            expungeStaleEntries()
            return super.remove(key)
        }

        override fun iterator(): MutableIterator<Map.Entry<K, V>> {
            expungeStaleEntries()
            return super.iterator()
        }

        override fun approximateSize(): Int {
            expungeStaleEntries()
            return super.approximateSize()
        }
    }

    private inner class EntryIterator(iterator: Iterator<Map.Entry<WeakKey<K>, V>>) :
        MutableIterator<Map.Entry<K, V>> {
        private val iterator: Iterator<Map.Entry<WeakKey<K?>, V>>

        private var nextEntry: Map.Entry<WeakKey<K?>, V>? = null

        private var nextKey: K? = null

        init {
            this.iterator = iterator
            findNext()
        }

        fun findNext() {
            while (iterator.hasNext()) {
                nextEntry = iterator.next()
                nextKey = nextEntry!!.key.get()
                if (nextKey != null) {
                    return
                }
            }
            nextEntry = null
            nextKey = null
        }

        override fun hasNext(): Boolean {
            return nextKey != null
        }

        override fun next(): Map.Entry<K, V> {
            if (nextKey == null) {
                throw NoSuchElementException()
            }
            try {
                return SimpleEntry(nextKey, nextEntry)
            } finally {
                findNext()
            }
        }

        override fun remove() {
            throw UnsupportedOperationException()
        }
    }

    private inner class SimpleEntry(
        override val key: K,
        val entry: MutableMap.MutableEntry<WeakKey<K>, V>
    ) :
        MutableMap.MutableEntry<K, V?> {
        override val value: V
            get() = entry.value

        override fun setValue(value: V?): V? {
            if (value == null) {
                throw NullPointerException()
            }
            return entry.setValue(value)
        }
    }

    companion object {
        private val ID = AtomicLong()
    }
}