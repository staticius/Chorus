package org.chorus.utils

import cn.powernukkitx.libdeflate.LibdeflateCompressor
import org.chorus.Server

class LibDeflator @JvmOverloads constructor(
    level: Int = Server.instance.settings.networkSettings.compressionLevel
) : LibdeflateCompressor(level)
