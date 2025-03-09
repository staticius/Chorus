package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockStickyPistonArmCollision @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockPistonArmCollision(blockstate) {
    override val name: String
        get() = "Sticky Piston Head"

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STICKY_PISTON_ARM_COLLISION, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}