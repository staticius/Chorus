package org.chorus.utils

import cn.nukkit.Nukkit
import java.io.File

object StartArgUtils {
    @JvmStatic
    val isValidStart: Boolean
        get() {
            return try {
                Class.forName("java.lang.ClassLoader").module
                    .isOpen("java.lang", Thread.currentThread().contextClassLoader.unnamedModule)
            } catch (e: ClassNotFoundException) {
                false
            }
        }

    @JvmStatic
    val isShaded: Boolean
        get() {
            val path = Nukkit::class.java.protectionDomain.codeSource.location.path
            val jarFile = File(path)
            if (jarFile.name.contains("shaded")) {
                return true
            }
            if (jarFile.exists()) {
                return jarFile.length() > 1024 * 1024 * 64
            }
            return false
        }
}
