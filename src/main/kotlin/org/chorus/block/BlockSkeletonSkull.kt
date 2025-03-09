package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.Item

class BlockSkeletonSkull(blockState: BlockState?) : BlockHead(blockState) {
    override val name: String
        get() = "Skeleton Skull"

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(
            this.toItem()
        )
    }

    override fun toItem(): Item? {
        return ItemSkeletonSkull()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SKELETON_SKULL, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}
