package org.chorus.utils

import cn.nukkit.Server
import cn.powernukkitx.libdeflate.LibdeflateCompressor

class PNXLibDeflater @JvmOverloads constructor(
    level: Int = Server.getInstance().settings.networkSettings().compressionLevel()
) :
    LibdeflateCompressor(level)
