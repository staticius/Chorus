package org.chorus.block

class BlockDarkOakPlanks @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockPlanks(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DARK_OAK_PLANKS)

    }
}