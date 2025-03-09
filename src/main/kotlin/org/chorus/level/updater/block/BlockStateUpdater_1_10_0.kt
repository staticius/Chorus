package org.chorus.level.updater.block

import cn.nukkit.level.updater.Updater
import cn.nukkit.level.updater.util.tagupdater.CompoundTagUpdaterContext
import lombok.AccessLevel
import lombok.NoArgsConstructor

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class BlockStateUpdater_1_10_0 : Updater {
    override fun registerUpdaters(context: CompoundTagUpdaterContext) {
        // TODO: mapped types. (I'm not sure if these are needed)
    }

    companion object {
        val INSTANCE: Updater = BlockStateUpdater_1_10_0()
    }
}
