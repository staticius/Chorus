package org.chorus.utils

import java.lang.ref.Cleaner

class CleanerHandle<RESOURCE : AutoCloseable?>(val resource: RESOURCE) {
    private val cleanable: Cleaner.Cleanable

    private class CleanerTask<RESOURCE : AutoCloseable?>(private val resource: RESOURCE) : Runnable {
        override fun run() {
            try {
                resource!!.close()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

    init {
        this.cleanable = CLEANER.register(this, CleanerTask(resource))
    }

    companion object {
        private val CLEANER: Cleaner = Cleaner.create()
    }
}
