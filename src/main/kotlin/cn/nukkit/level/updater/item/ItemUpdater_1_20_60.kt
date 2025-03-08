package cn.nukkit.level.updater.item

import cn.nukkit.level.updater.Updater
import cn.nukkit.level.updater.util.tagupdater.CompoundTagEditHelper
import cn.nukkit.level.updater.util.tagupdater.CompoundTagUpdaterContext
import kotlin.collections.set

class ItemUpdater_1_20_60 : Updater {
    override fun registerUpdaters(ctx: CompoundTagUpdaterContext) {
        ctx.addUpdater(1, 20, 60)
            .match("Name", "minecraft:scute")
            .edit(
                "Name"
            ) { helper: CompoundTagEditHelper ->
                helper.rootTag["Name"] = "minecraft:turtle_scute"
            }
    }

    companion object {
        val INSTANCE: Updater = ItemUpdater_1_20_60()
    }
}
