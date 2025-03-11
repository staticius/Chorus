package org.chorus.block

open class BlockBrainCoral @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCoral(blockstate) {
    override fun isDead(): Boolean {
        return false
    }

    override fun getDeadCoral(): Block {
        return BlockDeadBrainCoral()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BRAIN_CORAL)
            
    }
}