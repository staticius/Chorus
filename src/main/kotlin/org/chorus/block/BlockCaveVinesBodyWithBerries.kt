package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemID

class BlockCaveVinesBodyWithBerries @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockCaveVines(blockState) {
    override val name: String
        get() = "Cave Vines Body With Berries"

    override val isTransparent: Boolean
        get() = true

    override val lightLevel: Int
        get() = 14

    override fun getDrops(item: Item): Array<Item> {
        return arrayOf(Item.get(ItemID.GLOW_BERRIES))
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.CAVE_VINES_BODY_WITH_BERRIES, CommonBlockProperties.GROWING_PLANT_AGE)
    }
}
