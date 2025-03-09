package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockChemistryTable @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.CHEMISTRY_TABLE,
            CommonBlockProperties.CHEMISTRY_TABLE_TYPE,
            CommonBlockProperties.DIRECTION
        )
            get() = Companion.field
    }
}