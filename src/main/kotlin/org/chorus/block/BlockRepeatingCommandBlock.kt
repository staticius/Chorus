package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockRepeatingCommandBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCommandBlock(blockstate) {
    override val name: String
        get() = "Repeating Command Block"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.REPEATING_COMMAND_BLOCK,
            CommonBlockProperties.CONDITIONAL_BIT,
            CommonBlockProperties.FACING_DIRECTION
        )
            get() = Companion.field
    }
}