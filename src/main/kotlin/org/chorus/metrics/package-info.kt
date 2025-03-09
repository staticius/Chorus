/**
 * 与[bStats插件](https://bstats.org/)相关的类.
 *
 *
 * Classes relevant to [bStats Plugin](https://bstats.org/).
 */
package org.chorus.metrics

import org.chorus.config.ServerProperties.get
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import org.chorus.metrics.Metrics.CustomChart
import org.chorus.utils.MainLogger
import java.io.IOException
import javax.net.ssl.HttpsURLConnection
import org.chorus.metrics.NukkitMetrics
import java.util.HashMap
import java.io.BufferedWriter
import java.io.FileWriter
import java.util.concurrent.atomic.AtomicReference
import java.util.function.UnaryOperator
import org.chorus.metrics.Metrics.SingleLineChart
import org.chorus.metrics.Metrics.SimplePie
import org.chorus.config.ServerPropertiesKeys
import org.chorus.metrics.Metrics.AdvancedPie
import org.chorus.utils.LoginChainData
import org.chorus.utils.NukkitCollectors
import org.chorus.metrics.Metrics.DrilldownPie
import org.chorus.metrics.NukkitMetrics.JavaVersionRetriever

