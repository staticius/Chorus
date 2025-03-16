package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockCrafter @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    Block(blockstate,) {
    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.CRAFTER,
            CommonBlockProperties.CRAFTING,
            CommonBlockProperties.ORIENTATION,
            CommonBlockProperties.TRIGGERED_BIT
        )

    }
}