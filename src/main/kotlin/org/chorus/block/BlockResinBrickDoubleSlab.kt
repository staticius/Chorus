package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockResinBrickDoubleSlab(blockstate: BlockState?) : BlockDoubleWoodenSlab(blockstate) {
    override val slabName: String
        get() = "Resin Brick"

    override val singleSlab: BlockState
        get() = BlockResinBrickSlab.Companion.PROPERTIES.getDefaultState()

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.RESIN_BRICK_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}