package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockVault(blockstate: BlockState?) : Block(blockstate,) {
    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.BlockID.VAULT,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OMINOUS,
            CommonBlockProperties.VAULT_STATE
        )

    }
}