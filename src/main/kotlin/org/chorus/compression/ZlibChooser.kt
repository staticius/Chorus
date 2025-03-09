package org.chorus.compression

import cn.nukkit.Server
import cn.nukkit.lang.BaseLang
import cn.nukkit.utils.*
import cn.powernukkitx.libdeflate.Libdeflate
import lombok.extern.slf4j.Slf4j

@Slf4j
object ZlibChooser {
    private val providers = arrayOfNulls<ZlibProvider>(4)
    var currentProvider: ZlibProvider?
        private set

    init {
        providers[2] = ZlibThreadLocal()
        currentProvider = providers[2]
    }

    @JvmStatic
    fun setProvider(providerIndex: Int) {
        var providerIndex = providerIndex
        val lang = if (Server.getInstance() == null) BaseLang("eng") else Server.getInstance().language
        when (providerIndex) {
            0 -> if (providers[providerIndex] == null) providers[providerIndex] = ZlibOriginal()
            1 -> if (providers[providerIndex] == null) providers[providerIndex] = ZlibSingleThreadLowMem()
            2 -> {
                if (providers[providerIndex] == null) providers[providerIndex] = ZlibThreadLocal()
                if (Libdeflate.isAvailable()) {
                    ZlibChooser.log.info("{}{}", TextFormat.WHITE, lang.tr("nukkit.zlib.acceleration-can-enable"))
                }
            }

            3 -> if (Libdeflate.isAvailable()) {
                if (providers[providerIndex] == null) {
                    val libDeflateThreadLocal = LibDeflateThreadLocal(providers[2] as ZlibThreadLocal?)
                    providers[providerIndex] = libDeflateThreadLocal
                }
            } else {
                ZlibChooser.log.warn(lang.tr("nukkit.zlib.unavailable"))
                providerIndex = 2
                if (providers[providerIndex] == null) providers[providerIndex] = ZlibThreadLocal()
            }

            else -> throw UnsupportedOperationException("Invalid provider: $providerIndex")
        }
        if (providerIndex < 2) {
            ZlibChooser.log.warn(lang.tr("nukkit.zlib.affect-performance"))
        }
        currentProvider = providers[providerIndex]
        ZlibChooser.log.info(
            "{}: {} ({})",
            lang.tr("nukkit.zlib.selected"),
            providerIndex,
            currentProvider!!.javaClass.canonicalName
        )
    }
}
