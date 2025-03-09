package org.chorus.level.updater.block

import org.chorus.level.updater.Updater
import org.chorus.level.updater.util.tagupdater.CompoundTagUpdaterContext
import lombok.AccessLevel
import lombok.NoArgsConstructor

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class BlockStateUpdater_1_12_0 : Updater {
    override fun registerUpdaters(context: CompoundTagUpdaterContext) {
        context.addUpdater(1, 12, 0)
            .match("name", "minecraft:coral_fan")
            .visit("states")
            .rename("direction", "coral_fan_direction")

        context.addUpdater(1, 12, 0)
            .match("name", "minecraft:coral_fan_dead")
            .visit("states")
            .rename("direction", "coral_fan_direction")
    }

    companion object {
        val INSTANCE: Updater = BlockStateUpdater_1_12_0()
    }
}
