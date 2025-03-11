package org.chorus.block

class BlockAcaciaPlanks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockPlanks(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ACACIA_PLANKS)
    }
}