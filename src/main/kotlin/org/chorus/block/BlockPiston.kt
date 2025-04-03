package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.math.BlockFace

class BlockPiston @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockPistonBase(blockstate) {
    override val name: String
        get() = "Piston"

    override fun createHead(blockFace: BlockFace): Block {
        return BlockPistonArmCollision().setPropertyValue<Int, IntPropertyType>(
            CommonBlockProperties.FACING_DIRECTION,
            blockFace.index
        )
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PISTON, CommonBlockProperties.FACING_DIRECTION)
    }
}
