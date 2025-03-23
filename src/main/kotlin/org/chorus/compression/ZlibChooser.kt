package org.chorus.compression

import cn.powernukkitx.libdeflate.Libdeflate
import org.chorus.Server
import org.chorus.utils.*


object ZlibChooser : Loggable {
    private val providers = arrayOfNulls<ZlibProvider>(4)
    var currentProvider: ZlibProvider
        private set

    init {
        providers[2] = ZlibThreadLocal()
        currentProvider = providers[2]!!
    }

    @JvmStatic
    fun setProvider(providerIndex: Int) {
        var providerIndex1 = providerIndex
        val lang = Server.instance.baseLang
        when (providerIndex1) {
            0 -> if (providers[providerIndex1] == null) providers[providerIndex1] = ZlibOriginal()
            1 -> if (providers[providerIndex1] == null) providers[providerIndex1] = ZlibSingleThreadLowMem()
            2 -> {
                if (providers[providerIndex1] == null) providers[providerIndex1] = ZlibThreadLocal()
                if (Libdeflate.isAvailable()) {
                    ZlibChooser.log.info("{}{}", TextFormat.WHITE, lang.tr("nukkit.zlib.acceleration-can-enable"))
                }
            }

            3 -> if (Libdeflate.isAvailable()) {
                if (providers[providerIndex1] == null) {
                    val libDeflateThreadLocal = LibDeflateThreadLocal(providers[2] as ZlibThreadLocal?)
                    providers[providerIndex1] = libDeflateThreadLocal
                }
            } else {
                ZlibChooser.log.warn(lang.tr("nukkit.zlib.unavailable"))
                providerIndex1 = 2
                if (providers[providerIndex1] == null) providers[providerIndex1] = ZlibThreadLocal()
            }

            else -> throw UnsupportedOperationException("Invalid provider: $providerIndex1")
        }
        if (providerIndex1 < 2) {
            ZlibChooser.log.warn(lang.tr("nukkit.zlib.affect-performance"))
        }
        currentProvider = providers[providerIndex1]!!
        ZlibChooser.log.info(
            "{}: {} ({})",
            lang.tr("nukkit.zlib.selected"),
            providerIndex1,
            currentProvider.javaClass.canonicalName
        )
    }
}
