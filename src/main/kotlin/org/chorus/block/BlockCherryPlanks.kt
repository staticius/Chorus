package org.chorus.block

class BlockCherryPlanks @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockPlanks(blockState) {
    override val name: String
        get() = "Cherry Planks"

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHERRY_PLANKS)
    }
}