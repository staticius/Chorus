package org.chorus.utils

import lombok.extern.slf4j.Slf4j
import java.lang.ref.Reference
import java.lang.reflect.Array
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque


abstract class IterableThreadLocal<T> : ThreadLocal<T?>(), Iterable<T> {
    private val flag: ThreadLocal<T>? = null
    private val allValues = ConcurrentLinkedDeque<T>()

    override fun initialValue(): T? {
        val value = init()
        if (value != null) {
            allValues.add(value)
        }
        return value
    }

    override fun iterator(): MutableIterator<T> {
        return all.iterator()
    }

    open fun init(): T? {
        return null
    }

    fun clean() {
        clean(this)
    }

    val all: MutableCollection<T>
        get() = Collections.unmodifiableCollection(allValues)

    companion object {
        fun clean(instance: ThreadLocal<*>?) {
            try {
                var rootGroup = Thread.currentThread().threadGroup
                var parentGroup: ThreadGroup
                while ((rootGroup.parent.also { parentGroup = it }) != null) {
                    rootGroup = parentGroup
                }
                var threads = arrayOfNulls<Thread>(rootGroup.activeCount())
                if (threads.size != 0) {
                    while (rootGroup.enumerate(threads, true) == threads.size) {
                        threads = arrayOfNulls(threads.size * 2)
                    }
                }
                val tl = Thread::class.java.getDeclaredField("threadLocals")
                tl.isAccessible = true
                var methodRemove: Method? = null
                for (thread in threads) {
                    if (thread != null) {
                        val tlm = tl[thread]
                        if (tlm != null) {
                            if (methodRemove == null) {
                                methodRemove = tlm.javaClass.getDeclaredMethod("remove", ThreadLocal::class.java)
                                methodRemove.isAccessible = true
                            }
                            if (methodRemove != null) {
                                try {
                                    methodRemove.invoke(tlm, instance)
                                } catch (ignore: Throwable) {
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                IterableThreadLocal.log.error("", e)
            }
        }

        fun cleanAll() {
            try {
                // Get a reference to the thread locals table of the current thread
                val thread = Thread.currentThread()
                val threadLocalsField = Thread::class.java.getDeclaredField("threadLocals")
                threadLocalsField.isAccessible = true
                val threadLocalTable = threadLocalsField[thread]

                // Get a reference to the array holding the thread local variables inside the
                // ThreadLocalMap of the current thread
                val threadLocalMapClass = Class.forName("java.lang.ThreadLocal\$ThreadLocalMap")
                val tableField = threadLocalMapClass.getDeclaredField("table")
                tableField.isAccessible = true
                val table = tableField[threadLocalTable]

                // The key to the ThreadLocalMap is a WeakReference object. The referent field of this object
                // is a reference to the actual ThreadLocal variable
                val referentField = Reference::class.java.getDeclaredField("referent")
                referentField.isAccessible = true

                for (i in 0..<Array.getLength(table)) {
                    // Each entry in the table array of ThreadLocalMap is an Entry object
                    // representing the thread local reference and its value
                    val entry = Array.get(table, i)
                    if (entry != null) {
                        // Get a reference to the thread local object and remove it from the table
                        val threadLocal = referentField[entry] as ThreadLocal<*>
                        clean(threadLocal)
                    }
                }
            } catch (e: Exception) {
                // We will tolerate an exception here and just log it
                throw IllegalStateException(e)
            }
        }
    }
}
