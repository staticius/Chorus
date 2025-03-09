package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockChainCommandBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCommandBlock(blockstate) {
    override val name: String
        get() = "Chain Command Block"

    companion object {
        val properties: BlockProperties = BlockProperties(
            CHAIN_COMMAND_BLOCK,
            CommonBlockProperties.CONDITIONAL_BIT,
            CommonBlockProperties.FACING_DIRECTION
        )
            get() = Companion.field
    }
}