package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.math.BlockFace

class BlockStickyPiston @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockPistonBase(blockstate) {
    init {
        sticky = true
    }

    public override fun createHead(blockFace: BlockFace): Block {
        return BlockStickyPistonArmCollision().setPropertyValue<Int, IntPropertyType>(
            CommonBlockProperties.FACING_DIRECTION,
            blockFace.index
        )
    }

    override val name: String
        get() = "Sticky Piston"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.STICKY_PISTON, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}