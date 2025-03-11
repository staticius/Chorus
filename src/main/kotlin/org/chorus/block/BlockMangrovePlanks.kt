package org.chorus.block

class BlockMangrovePlanks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockPlanks(blockstate) {
    override val name: String
        get() = "Mangrove Planks"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MANGROVE_PLANKS)

    }
}