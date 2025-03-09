package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.item.Item
import cn.nukkit.item.ItemBlock
import cn.nukkit.level.Level
import cn.nukkit.math.BlockFace

abstract class BlockCoralWallFan(blockstate: BlockState?) : BlockCoralFan(blockstate) {
    override val name: String
        get() = super.name + " Wall Fan"

    override fun onUpdate(type: Int): Int {
        return if (type == Level.BLOCK_UPDATE_RANDOM) {
            type
        } else {
            super.onUpdate(type)
        }
    }

    override var blockFace: BlockFace?
        get() {
            val face =
                getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.CORAL_DIRECTION)
            return when (face) {
                0 -> BlockFace.WEST
                1 -> BlockFace.EAST
                2 -> BlockFace.NORTH
                else -> BlockFace.SOUTH
            }
        }
        set(blockFace) {
            super.blockFace = blockFace
        }

    override val rootsFace: BlockFace?
        get() = blockFace!!.getOpposite()

    override fun toItem(): Item? {
        return ItemBlock(if (isDead) deadCoralFan else this)
    }
}