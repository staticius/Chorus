package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.entity.mob.monster.EntityWither.Companion.checkAndSpawnWither
import org.chorus.item.Item
import org.chorus.math.BlockFace

class BlockWitherSkeletonSkull(blockState: BlockState?) : BlockHead(blockState) {
    override val name: String
        get() = "Wither Skeleton Skull"

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(
            this.toItem()
        )
    }

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        if (super.place(item, block, target, face, fx, fy, fz, player)) {
            EntityWither.checkAndSpawnWither(this)
            return true
        }
        return false
    }

    override fun toItem(): Item? {
        return ItemWitherSkeletonSkull()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WITHER_SKELETON_SKULL, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}
