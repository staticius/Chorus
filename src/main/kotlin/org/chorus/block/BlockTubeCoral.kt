package org.chorus.block

open class BlockTubeCoral @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockCoral(blockstate) {
    override val isDead: Boolean
        get() = false

    override val deadCoral: Block
        get() = BlockDeadTubeCoral()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.TUBE_CORAL)

    }
}