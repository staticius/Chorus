package org.chorus.level.updater.block

import org.chorus.level.updater.Updater
import org.chorus.level.updater.util.tagupdater.CompoundTagUpdaterContext
import lombok.AccessLevel
import lombok.NoArgsConstructor

(access = AccessLevel.PRIVATE)
class BlockStateUpdater_1_10_0 : Updater {
    override fun registerUpdaters(context: CompoundTagUpdaterContext) {
        // TODO: mapped types. (I'm not sure if these are needed)
    }

    companion object {
        val INSTANCE: Updater = BlockStateUpdater_1_10_0()
    }
}
