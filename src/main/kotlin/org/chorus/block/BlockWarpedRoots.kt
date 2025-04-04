package org.chorus.block

class BlockWarpedRoots @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockHanging(blockstate), BlockFlowerPot.FlowerPotBlock, Natural {
    override val name: String
        get() = "Warped Roots"

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WARPED_ROOTS)
    }
}