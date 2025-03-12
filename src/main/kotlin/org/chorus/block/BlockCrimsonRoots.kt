package org.chorus.block

import org.chorus.block.BlockFlowerPot.FlowerPotBlock

class BlockCrimsonRoots @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHanging(blockstate), FlowerPotBlock, Natural {
    override val name: String
        get() = "Crimson Roots"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CRIMSON_ROOTS)

    }
}