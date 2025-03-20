package org.chorus.block

class BlockDeadFireCoral @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockFireCoral(blockstate) {

    override fun isDead() = true

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.DEAD_FIRE_CORAL)
    }
}