package cn.nukkit.block

import cn.nukkit.block.property.enums.OxidizationLevel

open class BlockOxidizedCutCopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCutCopper(blockstate) {
    override val name: String
        get() = "Cut Oxidized Copper"

    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.OXIDIZED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OXIDIZED_CUT_COPPER)
            get() = Companion.field
    }
}