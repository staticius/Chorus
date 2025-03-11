package org.chorus.block

class BlockWaxedExposedCutCopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockExposedCutCopper(blockstate) {
    override val name: String
        get() = "Waxed Exposed Cut Copper"

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_EXPOSED_CUT_COPPER)

    }
}