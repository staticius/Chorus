package org.chorus.block

class BlockWaxedOxidizedCopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockOxidizedCopper(blockstate) {
    override val name: String
        get() = "Waxed Oxidized Copper"

    override fun isWaxed(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WAXED_OXIDIZED_COPPER)

    }
}