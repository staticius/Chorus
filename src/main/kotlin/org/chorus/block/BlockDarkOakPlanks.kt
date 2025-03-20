package org.chorus.block

class BlockDarkOakPlanks @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockPlanks(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DARK_OAK_PLANKS)
    }
}