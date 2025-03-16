package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemZombieHead

class BlockZombieHead(blockState: BlockState) : BlockHead(blockState) {
    override val name: String
        get() = "Zombie Head"

    override fun getDrops(item: Item): Array<Item> {
        return arrayOf(
            this.toItem()
        )
    }

    override fun toItem(): Item {
        return ItemZombieHead()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ZOMBIE_HEAD, CommonBlockProperties.FACING_DIRECTION)

    }
}
