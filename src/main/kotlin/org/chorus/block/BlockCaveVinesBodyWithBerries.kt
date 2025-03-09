package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

class BlockCaveVinesBodyWithBerries @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCaveVines(blockstate) {
    override val name: String
        get() = "Cave Vines Body With Berries"

    override val isTransparent: Boolean
        get() = true

    override val lightLevel: Int
        get() = 14

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(Item.get(ItemID.GLOW_BERRIES))
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(CAVE_VINES_BODY_WITH_BERRIES, CommonBlockProperties.GROWING_PLANT_AGE)
            get() = Companion.field
    }
}
