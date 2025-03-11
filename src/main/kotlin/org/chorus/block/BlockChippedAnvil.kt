package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockChippedAnvil @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockAnvil(blockstate) {
    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.CHIPPED_ANVIL, CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION)

    }
}
