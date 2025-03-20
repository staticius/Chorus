package org.chorus.block

open class BlockHornCoral @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCoral(blockstate) {

    override fun isDead() = false

    override fun getDeadCoral() = BlockDeadHornCoral()

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HORN_CORAL)
    }
}