package cn.nukkit.level.updater.item

import cn.nukkit.level.updater.Updater
import cn.nukkit.level.updater.util.tagupdater.CompoundTagEditHelper
import cn.nukkit.level.updater.util.tagupdater.CompoundTagUpdaterContext
import kotlin.collections.set

class ItemUpdater_1_20_70 : Updater {
    override fun registerUpdaters(ctx: CompoundTagUpdaterContext) {
        ctx.addUpdater(1, 20, 70)
            .match("Name", "minecraft:grass")
            .edit(
                "Name"
            ) { helper: CompoundTagEditHelper ->
                helper.rootTag["Name"] = "minecraft:grass_block"
            }
    }

    companion object {
        val INSTANCE: Updater = ItemUpdater_1_20_70()
    }
}
