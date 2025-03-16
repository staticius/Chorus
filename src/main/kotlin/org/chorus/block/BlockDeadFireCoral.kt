package org.chorus.block

class BlockDeadFireCoral @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockFireCoral(blockstate) {
    override val isDead: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DEAD_FIRE_CORAL)

    }
}