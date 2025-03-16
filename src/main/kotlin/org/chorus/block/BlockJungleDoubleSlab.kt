package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockJungleDoubleSlab(blockstate: BlockState) : BlockDoubleWoodenSlab(blockstate) {
    override val slabName: String
        get() = "Jungle"

    override val singleSlab: BlockState
        get() = BlockJungleSlab.Companion.PROPERTIES.getDefaultState()

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.Companion.JUNGLE_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}