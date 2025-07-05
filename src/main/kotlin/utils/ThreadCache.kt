package org.chorus_oss.chorus.utils

import java.io.ByteArrayOutputStream

object ThreadCache {
    val fbaos: ThreadLocal<ByteArrayOutputStream> = ThreadLocal.withInitial {
        ByteArrayOutputStream(1024)
    }
}
