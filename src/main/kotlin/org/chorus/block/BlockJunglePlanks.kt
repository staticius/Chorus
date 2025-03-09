package org.chorus.block

class BlockJunglePlanks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockPlanks(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.Companion.JUNGLE_PLANKS)
            get() = Companion.field
    }
}