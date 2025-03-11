package org.chorus.block

class BlockDeadHornCoral @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHornCoral(blockstate) {
    override val isDead: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DEAD_HORN_CORAL)

    }
}