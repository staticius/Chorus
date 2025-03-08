package cn.nukkit.level.updater.block

import cn.nukkit.level.updater.Updater
import cn.nukkit.level.updater.util.tagupdater.CompoundTagUpdaterContext

class BlockStateUpdater_1_17_40 : Updater {
    override fun registerUpdaters(context: CompoundTagUpdaterContext) {
        context.addUpdater(1, 16, 210, true) // Palette version wasn't bumped so far
            .match("name", "minecraft:sculk_catalyst")
            .visit("states")
            .tryAdd("bloom", 0.toByte())
    }

    companion object {
        val INSTANCE: Updater = BlockStateUpdater_1_17_40()
    }
}
