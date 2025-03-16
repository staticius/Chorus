package org.chorus.block

class BlockChiseledPolishedBlackstone @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockBlackstone(blockstate) {
    override val name: String
        get() = "Chiseled Polished Blackstone"

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHISELED_POLISHED_BLACKSTONE)

    }
}