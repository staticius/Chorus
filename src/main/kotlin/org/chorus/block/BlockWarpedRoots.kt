package org.chorus.block

class BlockWarpedRoots @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockHanging(blockstate), FlowerPotBlock, Natural {
    override val name: String
        get() = "Warped Roots"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WARPED_ROOTS)

    }
}