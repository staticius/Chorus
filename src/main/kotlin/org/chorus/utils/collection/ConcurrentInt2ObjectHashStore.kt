package org.chorus.utils.collection

import org.chorus.entity.data.EntityDataMap.size
import org.chorus.nbt.tag.ListTag.size
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

class ConcurrentInt2ObjectHashStore<T> {
    private val internalMap: Int2ObjectOpenHashMap<T>
    private val lock: ReadWriteLock
    private var defaultReturnValue: T

    constructor(expected: Int, l: Float) {
        this.internalMap = Int2ObjectOpenHashMap(expected, l)
        this.lock = ReentrantReadWriteLock()
        this.defaultReturnValue = null
    }

    constructor(expected: Int, l: Float, defaultReturnValue: T) {
        this.internalMap = Int2ObjectOpenHashMap(expected, l)
        this.lock = ReentrantReadWriteLock()
        this.defaultReturnValue = defaultReturnValue
    }

    fun size(): Int {
        val readLock = lock.readLock()
        try {
            return internalMap.size()
        } finally {
            readLock.unlock()
        }
    }

    fun clear() {
        val writeLock = lock.writeLock()
        while (true) {
            if (writeLock.tryLock()) {
                try {
                    internalMap.clear()
                    break
                } finally {
                    writeLock.unlock()
                }
            }
        }
    }

    val isEmpty: Boolean
        get() {
            val readLock = lock.readLock()
            try {
                return internalMap.isEmpty()
            } finally {
                readLock.unlock()
            }
        }

    fun containsValue(value: T): Boolean {
        val readLock = lock.readLock()
        while (true) {
            if (readLock.tryLock()) {
                try {
                    return internalMap.containsValue(value)
                } finally {
                    readLock.unlock()
                }
            }
        }
    }

    fun put(key: Int, value: T) {
        val writeLock = lock.writeLock()
        while (true) {
            if (writeLock.tryLock()) {
                try {
                    internalMap.put(key, value)
                    break
                } finally {
                    writeLock.unlock()
                }
            }
        }
    }

    fun defaultReturnValue(rv: T) {
        this.defaultReturnValue = rv
    }

    fun defaultReturnValue(): T {
        return defaultReturnValue
    }

    fun get(key: Int): T {
        val readLock = lock.readLock()
        while (true) {
            if (readLock.tryLock()) {
                try {
                    return internalMap[key]
                } finally {
                    readLock.unlock()
                }
            }
        }
    }

    fun containsKey(key: Int): Boolean {
        val readLock = lock.readLock()
        while (true) {
            if (readLock.tryLock()) {
                try {
                    return internalMap.containsKey(key)
                } finally {
                    readLock.unlock()
                }
            }
        }
    }
}
