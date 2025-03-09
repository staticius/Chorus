package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.Item
import java.util.concurrent.ThreadLocalRandom

class BlockRedMushroomBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockMushroomBlock(blockstate) {
    override fun getDrops(item: Item): Array<Item?>? {
        return if (ThreadLocalRandom.current().nextInt(1, 20) == 1) {
            arrayOf(
                Item.get(BlockID.RED_MUSHROOM_BLOCK)
            )
        } else {
            Item.EMPTY_ARRAY
        }
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.RED_MUSHROOM_BLOCK, CommonBlockProperties.HUGE_MUSHROOM_BITS)
            get() = Companion.field
    }
}