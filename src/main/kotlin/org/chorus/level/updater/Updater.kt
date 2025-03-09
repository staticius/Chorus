package org.chorus.level.updater

import cn.nukkit.level.updater.util.tagupdater.CompoundTagUpdaterContext

interface Updater {
    fun registerUpdaters(context: CompoundTagUpdaterContext)
}
