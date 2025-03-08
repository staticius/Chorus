/**
 * 与[bStats插件](https://bstats.org/)相关的类.
 *
 *
 * Classes relevant to [bStats Plugin](https://bstats.org/).
 */
package cn.nukkit.metrics

import cn.nukkit.config.ServerProperties.get
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import cn.nukkit.metrics.Metrics.CustomChart
import cn.nukkit.utils.MainLogger
import java.io.IOException
import javax.net.ssl.HttpsURLConnection
import cn.nukkit.metrics.NukkitMetrics
import java.util.HashMap
import java.io.BufferedWriter
import java.io.FileWriter
import java.util.concurrent.atomic.AtomicReference
import java.util.function.UnaryOperator
import cn.nukkit.metrics.Metrics.SingleLineChart
import cn.nukkit.metrics.Metrics.SimplePie
import cn.nukkit.config.ServerPropertiesKeys
import cn.nukkit.metrics.Metrics.AdvancedPie
import cn.nukkit.utils.LoginChainData
import cn.nukkit.utils.NukkitCollectors
import cn.nukkit.metrics.Metrics.DrilldownPie
import cn.nukkit.metrics.NukkitMetrics.JavaVersionRetriever

