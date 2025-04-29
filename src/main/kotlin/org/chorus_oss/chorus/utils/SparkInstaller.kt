package org.chorus_oss.chorus.utils

import org.chorus_oss.chorus.Server
import java.io.File
import java.io.IOException
import java.nio.file.Files
import javax.annotation.Nonnull


object SparkInstaller : Loggable {
    @JvmStatic
    fun initSpark(@Nonnull server: Server): Boolean {
        var download = false
        val spark = server.pluginManager.getPlugin("spark")
        if (spark == null) {
            download = true
        }

        if (download) {
            try {
                SparkInstaller::class.java.classLoader.getResourceAsStream("spark.jar").use { `in` ->
                    checkNotNull(`in`)
                    val targetPath = File(server.pluginPath, "spark.jar")
                    if (!targetPath.exists()) {
                        try {
                            Files.copy(`in`, targetPath.toPath())
                            server.pluginManager.enablePlugin(server.pluginManager.loadPlugin(targetPath)!!)
                            log.info("Spark has been installed.")
                        } catch (e: Exception) {
                            log.warn("Failed to copy spark: {}", e.stackTrace.contentToString())
                        }
                    }
                }
            } catch (e: IOException) {
                log.warn("Failed to download spark: {}", e.stackTrace.contentToString())
            }
        }

        return download
    }
}
