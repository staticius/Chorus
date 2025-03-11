package org.chorus.metrics

import org.chorus.Player
import org.chorus.Server
import org.chorus.config.ServerPropertiesKeys
import org.chorus.metrics.Metrics.*
import org.chorus.utils.Config
import org.chorus.utils.LoginChainData
import org.chorus.utils.NukkitCollectors

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Function
import java.util.regex.Pattern
import java.util.stream.Collectors


class NukkitMetrics private constructor(private val server: Server, start: Boolean) {
    private var enabled = false
    private var serverUUID: String? = null
    private var logFailedRequests = false

    private var metrics: Metrics? = null

    init {
        try {
            this.loadConfig()
        } catch (e: Exception) {
            NukkitMetrics.log.warn("Failed to load the bStats configuration file", e)
        }

        if (start && enabled) {
            startNow(server)
        }
    }

    private class JavaVersionRetriever :
        Callable<Map<String, Map<String, Int>>> {
        // The following code can be attributed to the PaperMC project
        // https://github.com/PaperMC/Paper/blob/master/Spigot-Server-Patches/0005-Paper-Metrics.patch#L614
        override fun call(): Map<String, Map<String, Int>> {
            val map: MutableMap<String, Map<String, Int>> = HashMap()
            val javaVersion = System.getProperty("java.version")
            val entry: MutableMap<String, Int> = HashMap()
            entry[javaVersion] = 1

            // http://openjdk.java.net/jeps/223
            // Java decided to change their versioning scheme and in doing so modified the java.version system
            // property to return $major[.$minor][.$secuity][-ea], as opposed to 1.$major.0_$identifier
            // we can handle pre-9 by checking if the "major" is equal to "1", otherwise, 9+
            var majorVersion = javaVersion.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            val release: String

            val indexOf = javaVersion.lastIndexOf('.')

            if (majorVersion == "1") {
                release = "Java " + javaVersion.substring(0, indexOf)
            } else {
                // of course, it really wouldn't be all that simple if they didn't add a quirk, now would it
                // valid strings for the major may potentially include values such as -ea to deannotate a pre release
                val versionMatcher = Pattern.compile("\\d+").matcher(majorVersion)
                if (versionMatcher.find()) {
                    majorVersion = versionMatcher.group(0)
                }
                release = "Java $majorVersion"
            }
            map[release] = entry
            return map
        }
    }

    /**
     * Loads the bStats configuration.
     */
    @Throws(IOException::class)
    private fun loadConfig() {
        val bStatsFolder = File(server.pluginPath, "bStats")

        if (!bStatsFolder.exists() && !bStatsFolder.mkdirs()) {
            NukkitMetrics.log.warn("Failed to create bStats metrics directory")
            return
        }

        val configFile = File(bStatsFolder, "config.yml")
        if (!configFile.exists()) {
            writeFile(
                configFile,
                "# bStats collects some data for plugin authors like how many servers are using their plugins.",
                "# To honor their work, you should not disable it.",
                "# This has nearly no effect on the server performance!",
                "# Check out https://bStats.org/ to learn more :)",
                "enabled: true",
                "serverUuid: \"" + UUID.randomUUID() + "\"",
                "logFailedRequests: false"
            )
        }

        val config = Config(configFile, Config.YAML)

        // Load configuration
        this.enabled = config.getBoolean("enabled", true)
        this.serverUUID = config.getString("serverUuid")
        this.logFailedRequests = config.getBoolean("logFailedRequests", false)
    }

    @Throws(IOException::class)
    private fun writeFile(file: File, vararg lines: String) {
        BufferedWriter(FileWriter(file)).use { writer ->
            for (line in lines) {
                writer.write(line)
                writer.newLine()
            }
        }
    }

    private fun mapDeviceOSToString(os: Int): String {
        return when (os) {
            1 -> "Android"
            2 -> "iOS"
            3 -> "macOS"
            4 -> "Fire OS"
            5 -> "Gear VR"
            6 -> "HoloLens"
            7, 8 -> "Windows"
            9 -> "Dedicated"
            10 -> "tvOS"
            11 -> "PlayStation"
            12 -> "Switch"
            13 -> "Xbox"
            14 -> "Windows Phone"
            else -> "Unknown"
        }
    }

    companion object {
        private val metricsStarted = AtomicReference(emptyMap<Server, NukkitMetrics>())

        /**
         * Setup the nukkit metrics and starts it if it hadn't started yet.
         *
         * @param server The Nukkit server
         */
        fun startNow(server: Server): Boolean {
            val nukkitMetrics = getOrCreateMetrics(server)
            return nukkitMetrics.metrics != null
        }

        private fun getOrCreateMetrics(server: Server): NukkitMetrics {
            var current = metricsStarted.get()
            var metrics = current[server]
            if (metrics != null) {
                return metrics
            }

            current = metricsStarted.updateAndGet { before: Map<Server, NukkitMetrics> ->
                var mutable = before
                if (before.isEmpty()) {
                    mutable = WeakHashMap(1)
                }
                mutable.computeIfAbsent(server) { server: Server ->
                    createMetrics(
                        server
                    )
                }
                mutable
            }

            metrics = current[server]
            checkNotNull(metrics)
            return metrics
        }

        private var pnxCliVersion: String? = null

        private val pNXCLIVersion: String
            get() {
                if (pnxCliVersion != null) {
                    return pnxCliVersion!!
                }
                val version = System.getProperty("pnx.cli.version")
                if (version != null && !version.isBlank()) {
                    return version.also { pnxCliVersion = it }
                }
                val cliPath = System.getProperty("pnx.cli.path")
                if (cliPath == null || cliPath.isBlank()) {
                    return "No PNX-CLI".also { pnxCliVersion = it }
                }
                try {
                    val process = ProcessBuilder(cliPath, "-V").start()
                    process.waitFor(10, TimeUnit.MICROSECONDS)
                    val content =
                        String(process.inputStream.readAllBytes()).replace("\n", "")
                    if (content.isBlank() || !content.contains(".")) {
                        return "Unknown".also { pnxCliVersion = it }
                    }
                    return content.also { pnxCliVersion = it }
                } catch (ignored: IOException) {
                    return "Unknown".also { pnxCliVersion = it }
                } catch (ignored: InterruptedException) {
                    return "Unknown".also { pnxCliVersion = it }
                }
            }

        private fun createMetrics(server: Server): NukkitMetrics {
            val nukkitMetrics = NukkitMetrics(server, false)
            if (!nukkitMetrics.enabled) {
                return nukkitMetrics
            }

            val metrics = Metrics("PowerNukkitX", nukkitMetrics.serverUUID, nukkitMetrics.logFailedRequests)
            nukkitMetrics.metrics = metrics

            metrics.addCustomChart(SingleLineChart("players") { server.onlinePlayers.size })
            metrics.addCustomChart(SimplePie("minecraft_version") { server.version })
            metrics.addCustomChart(SimplePie("pnx_version") { server.bStatsNukkitVersion })
            metrics.addCustomChart(
                SimplePie(
                    "xbox_auth"
                ) { if (server.properties.get(ServerPropertiesKeys.XBOX_AUTH, true)) "Required" else "Not required" })

            metrics.addCustomChart(
                AdvancedPie(
                    "player_platform_pie"
                ) {
                    server.onlinePlayers.values.stream()
                        .map { obj: Player -> obj.loginChainData }
                        .map { obj: LoginChainData -> obj.deviceOS }
                        .collect(
                            Collectors.groupingBy<Int, String, Any, Int>(
                                Function { os: Int ->
                                    nukkitMetrics.mapDeviceOSToString(
                                        os
                                    )
                                }, NukkitCollectors.countingInt()
                            )
                        )
                })

            metrics.addCustomChart(
                AdvancedPie(
                    "player_game_version_pie"
                ) {
                    server.onlinePlayers.values.stream()
                        .map { obj: Player -> obj.loginChainData }
                        .collect(
                            Collectors.groupingBy<LoginChainData, String, Any, Int>(
                                Function { obj: LoginChainData -> obj.gameVersion },
                                NukkitCollectors.countingInt()
                            )
                        )
                })

            metrics.addCustomChart(DrilldownPie("java_version_pie", JavaVersionRetriever()))

            metrics.addCustomChart(SimplePie("pnx_cli_version") { pNXCLIVersion })
            return nukkitMetrics
        }

        fun closeNow(server: Server) {
            val nukkitMetrics = getOrCreateMetrics(server)
            if (nukkitMetrics.metrics != null) nukkitMetrics.metrics!!.close()
        }
    }
}
