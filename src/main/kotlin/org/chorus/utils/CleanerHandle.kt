package org.chorus.utils

import java.lang.ref.Cleaner

class CleanerHandle<R : AutoCloseable?>(val resource: R) {
    private val cleanable: Cleaner.Cleanable = CLEANER.register(this, CleanerTask(resource))

    private class CleanerTask<R : AutoCloseable?>(private val resource: R) : Runnable {
        override fun run() {
            try {
                resource!!.close()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

    companion object {
        private val CLEANER: Cleaner = Cleaner.create()
    }
}
