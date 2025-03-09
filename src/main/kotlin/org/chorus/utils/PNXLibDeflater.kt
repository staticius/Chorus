package org.chorus.utils

import org.chorus.Server
import cn.powernukkitx.libdeflate.LibdeflateCompressor

class PNXLibDeflater @JvmOverloads constructor(
    level: Int = Server.getInstance().settings.networkSettings().compressionLevel()
) :
    LibdeflateCompressor(level)
