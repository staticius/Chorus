package org.chorus.utils

import cn.powernukkitx.libdeflate.LibdeflateCompressor
import org.chorus.Server

class PNXLibDeflater @JvmOverloads constructor(
    level: Int = Server.instance.settings.networkSettings().compressionLevel()
) :
    LibdeflateCompressor(level)
