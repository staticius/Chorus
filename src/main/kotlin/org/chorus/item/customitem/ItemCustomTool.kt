package org.chorus.item.customitem

import cn.nukkit.item.ItemTool
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.registry.Registries

/**
 * @author lt_name
 */
abstract class ItemCustomTool(id: String) : ItemTool(id), CustomItem {
    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_WOODEN


    val speed: Int?
        get() {
            val nbt =
                Registries.ITEM.customItemDefinition[getId()]!!.nbt
            if (nbt == null || !nbt.getCompound("components").contains("minecraft:digger")) return null
            return nbt.getCompound("components")
                .getCompound("minecraft:digger")
                .getList("destroy_speeds", CompoundTag::class.java)[0].getInt("speed")
        }
}
