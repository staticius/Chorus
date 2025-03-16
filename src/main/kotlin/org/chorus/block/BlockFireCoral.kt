package org.chorus.block

open class BlockFireCoral @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCoral(blockstate) {
    override val isDead: Boolean
        get() = false

    override val deadCoral: Block
        get() = BlockDeadFireCoral()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.FIRE_CORAL)

    }
}