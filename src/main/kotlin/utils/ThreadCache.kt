package org.chorus_oss.chorus.utils

import java.io.ByteArrayOutputStream

object ThreadCache {
    fun clean() {
        fbaos.clean()
    }

    val fbaos: IterableThreadLocal<ByteArrayOutputStream> =
        object : IterableThreadLocal<ByteArrayOutputStream>() {
            override fun init(): ByteArrayOutputStream {
                return ByteArrayOutputStream(1024)
            }
        }
}
