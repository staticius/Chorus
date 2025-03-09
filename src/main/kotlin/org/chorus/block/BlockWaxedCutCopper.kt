package org.chorus.block

class BlockWaxedCutCopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCutCopper(blockstate) {
    override val name: String
        get() = "Waxed Cut Copper"

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_CUT_COPPER)
            get() = Companion.field
    }
}