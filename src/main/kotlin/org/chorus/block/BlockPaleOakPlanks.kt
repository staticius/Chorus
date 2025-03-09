package org.chorus.block

class BlockPaleOakPlanks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockPlanks(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PALE_OAK_PLANKS)
            get() = Companion.field
    }
}