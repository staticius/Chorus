package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.Item

class BlockMushroomStem @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockMushroomBlock(blockstate) {
    override fun getDrops(item: Item): Array<Item?>? {
        return Item.EMPTY_ARRAY
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.MUSHROOM_STEM, CommonBlockProperties.HUGE_MUSHROOM_BITS)
            get() = Companion.field
    }
}