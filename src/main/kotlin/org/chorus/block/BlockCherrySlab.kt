package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockCherrySlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWoodenSlab(blockstate, CHERRY_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Cherry"
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.CHERRY_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}