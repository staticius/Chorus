/*
 * Originally from Mockito project.
 * ======================================
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.chorus.utils.collection

import java.util.function.Consumer

/**
 *
 *
 * A thread-safe set with weak values. Entries are based on a key's system hash code and keys are considered equal only by reference equality.
 *
 * This class does not implement the [java.util.Set] interface because this implementation is incompatible
 * with the set contract. While iterating over a set's entries, any value that has not passed iteration is referenced non-weakly.
 */
class WeakConcurrentSet<V>(cleaner: Cleaner) : Runnable,
    Iterable<V> {
    var target: WeakConcurrentMap<V, Boolean?>? = null
    val parallelismThreshold: Int

    init {
        target = when (cleaner) {
            Cleaner.INLINE -> WeakConcurrentMap.WithInlinedExpunction()
            Cleaner.THREAD, Cleaner.MANUAL -> WeakConcurrentMap(
                cleaner == Cleaner.THREAD
            )

            else -> throw AssertionError()
        }
        this.parallelismThreshold = Runtime.getRuntime().availableProcessors()
    }

    /**
     * @param value The value to add to the set.
     * @return `true` if the value was added to the set and was not contained before.
     */
    fun add(value: V): Boolean {
        return target!!.put(value, java.lang.Boolean.TRUE) == null // is null or Boolean.TRUE
    }

    /**
     * @param value The value to check if it is contained in the set.
     * @return `true` if the set contains the value.
     */
    fun contains(value: V): Boolean {
        return target!!.containsKey(value)
    }

    /**
     * @param value The value to remove from the set.
     * @return `true` if the value is contained in the set.
     */
    fun remove(value: V): Boolean {
        return target!!.remove(value) != null
    }

    /**
     * Clears the set.
     */
    fun clear() {
        target!!.clear()
    }

    /**
     * Returns the approximate size of this set where the returned number is at least as big as the actual number of entries.
     *
     * @return The minimum size of this set.
     */
    fun approximateSize(): Int {
        return target!!.approximateSize()
    }

    override fun run() {
        target!!.run()
    }

    /**
     * Determines the cleaning format. A reference is removed either by an explicitly started cleaner thread
     * associated with this instance ([Cleaner.THREAD]), as a result of interacting with this thread local
     * from any thread ([Cleaner.INLINE] or manually by submitting the detached thread local to a thread
     * ([Cleaner.MANUAL]).
     */
    enum class Cleaner {
        THREAD,
        INLINE,
        MANUAL
    }

    /**
     * Cleans all unused references.
     */
    fun expungeStaleEntries() {
        target!!.expungeStaleEntries()
    }

    val cleanerThread: Thread?
        /**
         * @return The cleaner thread or `null` if no such thread was set.
         */
        get() = target.getCleanerThread()

    override fun iterator(): MutableIterator<V> {
        return ReducingIterator(target!!.iterator())
    }

    fun parallelForeach(action: Consumer<in V?>) {
        target!!.target.forEachKey(
            parallelismThreshold.toLong(),
            { obj: WeakConcurrentMap.WeakKey<V?>? -> obj!!.get() }, action
        )
    }

    fun clearDeadReferences() {
        target!!.clearDeadReferences()
    }

    private class ReducingIterator<V>(private val iterator: MutableIterator<Map.Entry<V, Boolean?>?>) :
        MutableIterator<V> {
        override fun remove() {
            iterator.remove()
        }

        override fun next(): V {
            return iterator.next()!!.key
        }

        override fun hasNext(): Boolean {
            return iterator.hasNext()
        }
    }
}