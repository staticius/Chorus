package org.chorus.metrics

import org.chorus.utils.MainLogger
import io.netty.util.internal.EmptyArrays
import lombok.extern.slf4j.Slf4j
import org.jose4j.json.internal.json_simple.JSONArray
import org.jose4j.json.internal.json_simple.JSONObject
import java.io.*
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.concurrent.*
import java.util.zip.GZIPOutputStream
import javax.net.ssl.HttpsURLConnection

/**
 * bStats collects some data for plugin authors.
 *
 *
 * Check out https://bStats.org/ to learn more about bStats!
 */
@Slf4j
class Metrics(// The name of the server software
    private val name: String?, // The uuid of the server
    private val serverUUID: String?, logFailedRequests: Boolean
) {
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(
        1
    ) { t: Runnable? -> Thread(t, "metrics#scheduler") }

    // A list with all custom charts
    private val charts: MutableList<CustomChart> = ArrayList()

    // Should failed requests be logged?
    private val logFailedRequests: Boolean

    /**
     * Creates a new instance and starts submitting immediately.
     *
     * @param name              The bStats metrics identifier.
     * @param serverUUID        The unique identifier of this server.
     * @param logFailedRequests If failed submissions should be logged.
     * @param logger            The server main logger, ignored by PowerNukkit.
     */
    constructor(
        name: String?,
        serverUUID: String?,
        logFailedRequests: Boolean,
        @Suppress("unused") logger: MainLogger?
    ) : this(name, serverUUID, logFailedRequests)

    /**
     * Creates a new instance and starts submitting immediately.
     *
     * @param name              The bStats metrics identifier.
     * @param serverUUID        The unique identifier of this server.
     * @param logFailedRequests If failed submissions should be logged.
     */
    init {
        this.serverUUID = serverUUID
        this.logFailedRequests = logFailedRequests

        // Start submitting the data
        startSubmitting()
    }

    /**
     * Adds a custom chart.
     *
     * @param chart The chart to add.
     */
    fun addCustomChart(chart: CustomChart) {
        requireNotNull(chart) { "Chart cannot be null!" }
        charts.add(chart)
    }

    /**
     * Starts the Scheduler which submits our data every 30 minutes.
     */
    private fun startSubmitting() {
        val submitTask = Runnable { this.submitData() }

        // Many servers tend to restart at a fixed time at xx:00 which causes an uneven distribution of requests on the
        // bStats backend. To circumvent this problem, we introduce some randomness into the initial and second delay.
        // WARNING: You must not modify and part of this Metrics class, including the submit delay or frequency!
        // WARNING: Modifying this code will get your plugin banned on bStats. Just don't do it!
        val initialDelay = (1000 * 60 * (3 + Math.random() * 3)).toLong()
        val secondDelay = (1000 * 60 * (Math.random() * 30)).toLong()
        scheduler.schedule(submitTask, initialDelay, TimeUnit.MILLISECONDS)
        scheduler.scheduleAtFixedRate(submitTask, initialDelay + secondDelay, 1000 * 60 * 30L, TimeUnit.MILLISECONDS)
    }

    private val pluginData: JSONObject
        /**
         * Gets the plugin specific data.
         *
         * @return The plugin specific data.
         */
        get() {
            val data =
                JSONObject()

            data["pluginName"] = name // Append the name of the server software
            val customCharts =
                JSONArray()
            for (customChart in charts) {
                // Add the data of the custom charts
                val chart = customChart.requestJsonObject
                    ?: // If the chart is null, we skip it
                    continue
                customCharts.add(chart)
            }
            data["customCharts"] = customCharts

            return data
        }

    private val serverData: JSONObject
        /**
         * Gets the server specific data.
         *
         * @return The server specific data.
         */
        get() {
            // OS specific data
            val osName = System.getProperty("os.name")
            val osArch = System.getProperty("os.arch")
            val osVersion = System.getProperty("os.version")
            val coreCount = Runtime.getRuntime().availableProcessors()

            val data =
                JSONObject()
            data["serverUUID"] = serverUUID
            data["osName"] = osName
            data["osArch"] = osArch
            data["osVersion"] = osVersion
            data["coreCount"] = coreCount
            return data
        }

    /**
     * Collects the data and sends it afterwards.
     */
    private fun submitData() {
        val data = serverData

        val pluginData = JSONArray()
        pluginData.add(this.pluginData)
        data["plugins"] = pluginData

        try {
            // We are still in the Thread of the timer, so nothing get blocked :)
            sendData(data)
        } catch (e: Exception) {
            // Something went wrong! :(
            if (logFailedRequests) {
                Metrics.log.warn("Could not submit stats of {}", name, e)
            }
        }
    }

    /**
     * Represents a custom chart.
     */
    abstract class CustomChart internal constructor(chartId: String) {
        // The id of the chart
        val chartId: String

        /**
         * Class constructor.
         *
         * @param chartId The id of the chart.
         */
        init {
            require(!(chartId == null || chartId.isEmpty())) { "ChartId cannot be null or empty!" }
            this.chartId = chartId
        }

        val requestJsonObject: JSONObject?
            get() {
                val chart =
                    JSONObject()
                chart["chartId"] = chartId
                try {
                    val data = chartData
                        ?: // If the data is null we don't send the chart.
                        return null
                    chart["data"] = data
                } catch (t: Exception) {
                    return null
                }
                return chart
            }

        @get:Throws(Exception::class)
        protected abstract val chartData: JSONObject?
    }

    /**
     * Represents a custom simple pie.
     */
    class SimplePie
    /**
     * Class constructor.
     *
     * @param chartId  The id of the chart.
     * @param callable The callable which is used to request the chart data.
     */(chartId: String, private val callable: Callable<String>) : CustomChart(chartId) {
        @get:Throws(Exception::class)
        override val chartData: JSONObject?
            get() {
                val data =
                    JSONObject()
                val value = callable.call()
                if (value == null || value.isEmpty()) {
                    // Null = skip the chart
                    return null
                }
                data["value"] = value
                return data
            }
    }

    /**
     * Represents a custom advanced pie.
     */
    class AdvancedPie
    /**
     * Class constructor.
     *
     * @param chartId  The id of the chart.
     * @param callable The callable which is used to request the chart data.
     */(chartId: String, private val callable: Callable<Map<String, Int>>) :
        CustomChart(chartId) {
        @get:Throws(Exception::class)
        override val chartData: JSONObject?
            get() = createAdvancedChartData(callable)
    }

    /**
     * Represents a custom drill down pie.
     */
    @Suppress("SpellCheckingInspection")
    class DrilldownPie
    /**
     * Class constructor.
     *
     * @param chartId  The id of the chart.
     * @param callable The callable which is used to request the chart data.
     */(chartId: String, private val callable: Callable<Map<String, Map<String, Int>>>) :
        CustomChart(chartId) {
        @get:Throws(Exception::class)
        override val chartData: JSONObject?
            get() {
                val data =
                    JSONObject()
                val values =
                    JSONObject()
                val map =
                    callable.call()
                if (map == null || map.isEmpty()) {
                    // Null = skip the chart
                    return null
                }
                var reallyAllSkipped = true
                for ((key) in map) {
                    val value =
                        JSONObject()
                    var allSkipped = true
                    for ((key1, value1) in map[key]!!) {
                        value[key1] = value1
                        allSkipped = false
                    }
                    if (!allSkipped) {
                        reallyAllSkipped = false
                        values[key] = value
                    }
                }
                if (reallyAllSkipped) {
                    // Null = skip the chart
                    return null
                }
                data[VALUES] = values
                return data
            }
    }

    /**
     * Represents a custom single line chart.
     */
    class SingleLineChart
    /**
     * Class constructor.
     *
     * @param chartId  The id of the chart.
     * @param callable The callable which is used to request the chart data.
     */(chartId: String, private val callable: Callable<Int>) : CustomChart(chartId) {
        @get:Throws(Exception::class)
        override val chartData: JSONObject?
            get() {
                val data =
                    JSONObject()
                val value = callable.call()
                if (value == 0) {
                    // Null = skip the chart
                    return null
                }
                data["value"] = value
                return data
            }
    }

    /**
     * Represents a custom multi line chart.
     */
    class MultiLineChart
    /**
     * Class constructor.
     *
     * @param chartId  The id of the chart.
     * @param callable The callable which is used to request the chart data.
     */(chartId: String, private val callable: Callable<Map<String, Int>>) :
        CustomChart(chartId) {
        @get:Throws(Exception::class)
        override val chartData: JSONObject?
            get() = createAdvancedChartData(callable)
    }

    /**
     * Represents a custom simple bar chart.
     */
    class SimpleBarChart
    /**
     * Class constructor.
     *
     * @param chartId  The id of the chart.
     * @param callable The callable which is used to request the chart data.
     */(chartId: String, private val callable: Callable<Map<String, Int>>) :
        CustomChart(chartId) {
        @get:Throws(Exception::class)
        override val chartData: JSONObject?
            get() {
                val data =
                    JSONObject()
                val values =
                    JSONObject()
                val map = callable.call()
                if (map == null || map.isEmpty()) {
                    // Null = skip the chart
                    return null
                }
                for ((key, value) in map) {
                    val categoryValues =
                        JSONArray()
                    categoryValues.add(value)
                    values[key] = categoryValues
                }
                data[VALUES] = values
                return data
            }
    }

    /**
     * Represents a custom advanced bar chart.
     */
    class AdvancedBarChart
    /**
     * Class constructor.
     *
     * @param chartId  The id of the chart.
     * @param callable The callable which is used to request the chart data.
     */(chartId: String, private val callable: Callable<Map<String, IntArray>>) :
        CustomChart(chartId) {
        @get:Throws(Exception::class)
        override val chartData: JSONObject?
            get() {
                val data =
                    JSONObject()
                val values =
                    JSONObject()
                val map = callable.call()
                if (map == null || map.isEmpty()) {
                    // Null = skip the chart
                    return null
                }
                var allSkipped = true
                for ((key, value) in map) {
                    if (value.size == 0) {
                        continue  // Skip this invalid
                    }
                    allSkipped = false
                    val categoryValues =
                        JSONArray()
                    for (categoryValue in value) {
                        categoryValues.add(categoryValue)
                    }
                    values[key] = categoryValues
                }
                if (allSkipped) {
                    // Null = skip the chart
                    return null
                }
                data[VALUES] = values
                return data
            }
    }

    fun close() {
        scheduler.shutdownNow()
    }

    companion object {
        const val B_STATS_VERSION: Int = 1
        private const val VALUES = "values"

        // The url to which the data is sent
        private const val URL = "https://bStats.org/submitData/server-implementation"

        /**
         * Sends the data to the bStats server.
         *
         * @param data The data to send.
         * @throws IOException If the request failed.
         */
        @Throws(IOException::class)
        private fun sendData(data: JSONObject) {
            requireNotNull(data) { "Data cannot be null!" }

            val connection = URL(URL).openConnection() as HttpsURLConnection

            // Compress the data to save bandwidth
            val compressedData = compress(data.toString())

            // Add headers
            connection.requestMethod = "POST"
            connection.addRequestProperty("Accept", "application/json")
            connection.addRequestProperty("Connection", "close")
            connection.addRequestProperty("Content-Encoding", "gzip") // We gzip our request
            connection.addRequestProperty("Content-Length", compressedData.size.toString())
            connection.setRequestProperty("Content-Type", "application/json") // We send our data in JSON format
            connection.setRequestProperty("User-Agent", "MC-Server/" + B_STATS_VERSION)

            // Send data
            connection.doOutput = true
            val outputStream = DataOutputStream(connection.outputStream)
            outputStream.write(compressedData)
            outputStream.flush()
            outputStream.close()

            connection.inputStream.close() // We don't care about the response - Just send our data :)
        }

        /**
         * GZIPs the given String.
         *
         * @param str The string to gzip.
         * @return The gzipped String.
         * @throws IOException If the compression failed.
         */
        @Throws(IOException::class)
        private fun compress(str: String?): ByteArray {
            if (str == null) {
                return EmptyArrays.EMPTY_BYTES
            }
            val outputStream = ByteArrayOutputStream()
            val gzip = GZIPOutputStream(outputStream)
            gzip.write(str.toByteArray(StandardCharsets.UTF_8))
            gzip.close()
            return outputStream.toByteArray()
        }

        @Throws(Exception::class)
        private fun createAdvancedChartData(callable: Callable<Map<String, Int>>): JSONObject? {
            val data = JSONObject()
            val values = JSONObject()
            val map = callable.call()
            if (map == null || map.isEmpty()) {
                // Null = skip the chart
                return null
            }
            var allSkipped = true
            for ((key, value) in map) {
                if (value == 0) {
                    continue  // Skip this invalid
                }
                allSkipped = false
                values[key] = value
            }
            if (allSkipped) {
                // Null = skip the chart
                return null
            }
            data[VALUES] = values
            return data
        }
    }
}

