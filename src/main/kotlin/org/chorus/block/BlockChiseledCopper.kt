package org.chorus.block

import org.chorus.block.property.enums.OxidizationLevel

class BlockChiseledCopper @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockChiseledCopperBase(blockState) {
    override val oxidizationLevel: OxidizationLevel
        get() = OxidizationLevel.UNAFFECTED

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHISELED_COPPER)
    }
}