package org.chorus.block

class BlockWaxedOxidizedCutCopper @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockOxidizedCutCopper(blockstate) {
    override val name: String
        get() = "Waxed Oxidized Cut Copper"

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_OXIDIZED_CUT_COPPER)

    }
}