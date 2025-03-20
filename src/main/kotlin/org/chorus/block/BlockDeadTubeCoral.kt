package org.chorus.block

class BlockDeadTubeCoral @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockTubeCoral(blockstate) {

    override fun isDead() = true

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DEAD_TUBE_CORAL)
    }
}