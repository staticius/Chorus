package org.chorus.block

class BlockCherryPlanks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockPlanks(blockstate) {
    override val name: String
        get() = "Cherry Planks"

    companion object {
        val properties: BlockProperties = BlockProperties(CHERRY_PLANKS)
            get() = Companion.field
    }
}