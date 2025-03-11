package org.chorus.block

class BlockOakPlanks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockPlanks(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OAK_PLANKS)

    }
}