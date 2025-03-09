package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockVault(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.VAULT,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OMINOUS,
            CommonBlockProperties.VAULT_STATE
        )
            get() = Companion.field
    }
}