package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockDamagedAnvil @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockAnvil(blockstate) {
    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.DAMAGED_ANVIL, CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION)

    }
}
