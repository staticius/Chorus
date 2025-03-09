package org.chorus.level.updater

import org.chorus.level.updater.util.tagupdater.CompoundTagUpdaterContext

interface Updater {
    fun registerUpdaters(context: CompoundTagUpdaterContext)
}
