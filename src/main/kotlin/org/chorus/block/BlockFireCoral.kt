package org.chorus.block

open class BlockFireCoral @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCoral(blockstate) {
    override fun isDead() = false

    override fun getDeadCoral() = BlockDeadFireCoral()

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.FIRE_CORAL)
    }
}