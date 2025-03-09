package org.chorus

import org.chorus.nbt.stream.PGZIPOutputStream
import org.chorus.utils.MainLogger.error
import org.chorus.utils.Utils.dynamic
import com.google.common.base.Preconditions
import io.netty.util.ResourceLeakDetector
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.Log4J2LoggerFactory
import joptsimple.OptionParser
import joptsimple.OptionSpec
import lombok.extern.slf4j.Slf4j
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/*
* `_   _       _    _    _ _
* | \ | |     | |  | |  (_) |
* |  \| |_   _| | _| | ___| |_
* | . ` | | | | |/ / |/ / | __|
* | |\  | |_| |   <|   <| | |_
* |_| \_|\__,_|_|\_\_|\_\_|\__|
*/
/**
 * Nukkit启动类，包含`main`函数。<br></br>
 * The launcher class of Nukkit, including the `main` function.
 *
 * @author MagicDroidX(code) @ Nukkit Project
 * @author 粉鞋大妈(javadoc) @ Nukkit Project
 * @since Nukkit 1.0 | Nukkit API 1.0.0
 */
@Slf4j
object Nukkit {
    val GIT_INFO: Properties? = gitInfo
    val VERSION: String? = version
    val CODENAME: String = dynamic("PowerNukkitX")
    val GIT_COMMIT: String = gitCommit
    val API_VERSION: String = dynamic("2.0.0")
    val PATH: String = System.getProperty("user.dir") + "/"
    val DATA_PATH: String = System.getProperty("user.dir") + "/"
    val PLUGIN_PATH: String = DATA_PATH + "plugins"
    val START_TIME: Long = System.currentTimeMillis()
    var ANSI: Boolean = true
    var TITLE: Boolean = false
    var shortTitle: Boolean = requiresShortTitle()
    var DEBUG: Int = 1
    var CHROME_DEBUG_PORT: Int = -1
    var JS_DEBUG_LIST: List<String> = LinkedList()

    @JvmStatic
    fun main(args: Array<String>) {
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED)

        val disableSentry = AtomicBoolean(false)
        disableSentry.set(System.getProperty("disableSentry", "false").toBoolean())

        val propertiesPath = Paths.get(DATA_PATH, "server.properties")
        if (!disableSentry.get() && Files.isRegularFile(propertiesPath)) {
            val properties = Properties()
            try {
                FileReader(propertiesPath.toFile()).use { reader ->
                    properties.load(reader)
                    var value = properties.getProperty("disable-auto-bug-report", "false")
                    if (value.equals("on", ignoreCase = true) || value == "1") {
                        value = "true"
                    }
                    disableSentry.set(value.lowercase().toBoolean())
                }
            } catch (e: IOException) {
                Nukkit.log.error("Failed to load server.properties to check disable-auto-bug-report.", e)
            }
        }

        // Force IPv4 since Nukkit is not compatible with IPv6
        System.setProperty("java.net.preferIPv4Stack", "true")
        System.setProperty("log4j.skipJansi", "false")
        System.getProperties()
            .putIfAbsent("io.netty.allocator.type", "unpooled") // Disable memory pooling unless specified

        // Force Mapped ByteBuffers for LevelDB till fixed.
        System.setProperty("leveldb.mmap", "true")

        // Netty logger for debug info
        InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE)

        // Define args
        val parser = OptionParser()
        parser.allowsUnrecognizedOptions()
        val helpSpec: OptionSpec<Void> = parser.accepts("help", "Shows this page").forHelp()
        val ansiSpec: OptionSpec<Void> = parser.accepts("disable-ansi", "Disables console coloring")
        val titleSpec: OptionSpec<Void> = parser.accepts("enable-title", "Enables title at the top of the window")
        val vSpec: OptionSpec<String> = parser.accepts("v", "Set verbosity of logging").withRequiredArg().ofType(
            String::class.java
        )
        val verbositySpec: OptionSpec<String> =
            parser.accepts("verbosity", "Set verbosity of logging").withRequiredArg().ofType(
                String::class.java
            )
        val languageSpec: OptionSpec<String> =
            parser.accepts("language", "Set a predefined language").withOptionalArg().ofType(
                String::class.java
            )
        val chromeDebugPortSpec: OptionSpec<Int> =
            parser.accepts("chrome-debug", "Debug javascript using chrome dev tool with specific port.")
                .withRequiredArg().ofType(
                    Int::class.java
                )
        val jsDebugPortSpec: OptionSpec<String> =
            parser.accepts("js-debug", "Debug javascript using chrome dev tool with specific port.").withRequiredArg()
                .ofType(
                    String::class.java
                )

        // Parse arguments
        val options = parser.parse(*args)

        if (options.has(helpSpec)) {
            try {
                // Display help page
                parser.printHelpOn(System.out)
            } catch (e: IOException) {
                // ignore
            }
            return
        }

        ANSI = !options.has(ansiSpec)
        TITLE = options.has(titleSpec)

        var verbosity = options.valueOf(vSpec)
        if (verbosity == null) {
            verbosity = options.valueOf(verbositySpec)
        }
        if (verbosity != null) {
            try {
                val level = Level.valueOf(verbosity)
                logLevel = level
            } catch (e: Exception) {
                // ignore
            }
        }

        val language = options.valueOf(languageSpec)

        if (options.has(chromeDebugPortSpec)) {
            CHROME_DEBUG_PORT = options.valueOf(chromeDebugPortSpec)
        }

        if (options.has(jsDebugPortSpec)) {
            JS_DEBUG_LIST =
                Arrays.stream(options.valueOf(jsDebugPortSpec).split(",".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()).toList()
        }

        try {
            if (TITLE) {
                print(0x1b.toChar().toString() + "]0;PowerNukkitX is starting up..." + 0x07.toChar())
            }
            Server(PATH, DATA_PATH, PLUGIN_PATH, language)
        } catch (t: Throwable) {
            Nukkit.log.error("", t)
        }

        if (TITLE) {
            print(0x1b.toChar().toString() + "]0;Stopping Server..." + 0x07.toChar())
        }
        Nukkit.log.info("Stopping other threads")

        PGZIPOutputStream.getSharedThreadPool().shutdownNow()
        for (thread in Thread.getAllStackTraces().keys) {
            if (thread !is InterruptibleThread) {
                continue
            }
            Nukkit.log.debug("Stopping {} thread", thread.javaClass.simpleName)
            if (thread.isAlive) {
                thread.interrupt()
            }
        }

        if (TITLE) {
            print(0x1b.toChar().toString() + "]0;Server Stopped" + 0x07.toChar())
        }
        LogManager.shutdown()
        Runtime.getRuntime().halt(0) // force exit
    }

    private fun requiresShortTitle(): Boolean {
        //Shorter title for windows 8/2012
        val osName = System.getProperty("os.name").lowercase()
        return osName.contains("windows") && (osName.contains("windows 8") || osName.contains("2012"))
    }

    private val gitInfo: Properties?
        get() {
            var gitFileStream: InputStream? = null
            try {
                gitFileStream = Nukkit::class.java.module.getResourceAsStream("git.properties")
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            if (gitFileStream == null) {
                return null
            }
            val properties = Properties()
            try {
                properties.load(gitFileStream)
            } catch (e: IOException) {
                return null
            }
            return properties
        }

    private val version: String?
        get() {
            var resourceAsStream: InputStream? = null
            try {
                resourceAsStream = Nukkit::class.java.module.getResourceAsStream("git.properties")
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            if (resourceAsStream == null) {
                return "Unknown-PNX-SNAPSHOT"
            }
            val properties = Properties()
            try {
                resourceAsStream.use { `is` ->
                    InputStreamReader(`is`).use { reader ->
                        BufferedReader(reader).use { buffered ->
                            properties.load(buffered)
                            val line = properties.getProperty("git.build.version")
                            return if ("\${project.version}".equals(line, ignoreCase = true)) {
                                "Unknown-PNX-SNAPSHOT"
                            } else {
                                line
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                return "Unknown-PNX-SNAPSHOT"
            }
        }

    private val gitCommit: String
        get() {
            val version = StringBuilder()
            version.append("git-")
            val commitId: String
            if (GIT_INFO == null || (GIT_INFO.getProperty("git.commit.id.abbrev")
                    .also { commitId = it }) == null
            ) {
                return version.append("null").toString()
            }
            return version.append(commitId).toString()
        }

    var logLevel: Level?
        get() {
            val ctx =
                LogManager.getContext(false) as LoggerContext
            val log4jConfig = ctx.configuration
            val loggerConfig =
                log4jConfig.getLoggerConfig(LogManager.ROOT_LOGGER_NAME)
            return loggerConfig.level
        }
        set(level) {
            Preconditions.checkNotNull(level, "level")
            val ctx =
                LogManager.getContext(false) as LoggerContext
            val log4jConfig = ctx.configuration
            val loggerConfig =
                log4jConfig.getLoggerConfig(LogManager.ROOT_LOGGER_NAME)
            loggerConfig.level = level
            ctx.updateLoggers()
        }
}
