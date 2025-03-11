package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.*
import java.util.concurrent.ThreadLocalRandom

class BlockBrownMushroomBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockMushroomBlock(blockstate) {
    override fun getDrops(item: Item): Array<Item?>? {
        return if (ThreadLocalRandom.current().nextInt(1, 20) == 1) {
            arrayOf(
                Item.get(BROWN_MUSHROOM_BLOCK)
            )
        } else {
            Item.EMPTY_ARRAY
        }
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.BROWN_MUSHROOM_BLOCK, CommonBlockProperties.HUGE_MUSHROOM_BITS)

    }
}