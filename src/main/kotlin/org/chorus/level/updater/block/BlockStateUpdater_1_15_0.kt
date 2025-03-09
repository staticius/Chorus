package org.chorus.level.updater.block

import org.chorus.level.updater.Updater
import org.chorus.level.updater.util.tagupdater.CompoundTagEditHelper
import org.chorus.level.updater.util.tagupdater.CompoundTagUpdaterContext
import lombok.AccessLevel
import lombok.NoArgsConstructor

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class BlockStateUpdater_1_15_0 : Updater {
    override fun registerUpdaters(context: CompoundTagUpdaterContext) {
        context.addUpdater(1, 15, 0)
            .match("name", "minecraft:kelp")
            .visit("states")
            .edit("age") { helper: CompoundTagEditHelper ->
                val age = helper.tag as Int
                helper.replaceWith("kelp_age", age)
            }
    }

    companion object {
        val INSTANCE: Updater = BlockStateUpdater_1_15_0()
    }
}
