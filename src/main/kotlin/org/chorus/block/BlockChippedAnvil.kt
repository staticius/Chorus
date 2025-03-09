package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockChippedAnvil @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockAnvil(blockstate) {
    companion object {
        val properties: BlockProperties =
            BlockProperties(CHIPPED_ANVIL, CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION)
            get() = Companion.field
    }
}
