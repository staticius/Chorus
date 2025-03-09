package org.chorus.block

import cn.nukkit.block.property.enums.OxidizationLevel

open class BlockOxidizedCopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCopperBlock(blockstate) {
    override val name: String
        get() = "Oxidized Copper"

    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.OXIDIZED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OXIDIZED_COPPER)
            get() = Companion.field
    }
}