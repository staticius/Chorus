package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemHead
import org.chorus.item.ItemPiglinHead

class BlockPiglinHead(blockState: BlockState?) : BlockHead(blockState), ItemHead {
    override val name: String
        get() = "Piglin Head"

    override fun getDrops(item: Item): Array<Item?> {
        return arrayOf(
            this.toItem()
        )
    }

    override fun toItem(): Item {
        return ItemPiglinHead()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PIGLIN_HEAD, CommonBlockProperties.FACING_DIRECTION)

    }
}
