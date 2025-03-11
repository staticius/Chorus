package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType
import org.chorus.item.*

class BlockPaleOakLeaves(blockstate: BlockState?) : BlockLeaves(blockstate) {
    override val type: WoodType
        get() = WoodType.PALE_OAK

    override fun toSapling(): Item {
        return Item.get(BlockID.PALE_OAK_SAPLING)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.BlockID.PALE_OAK_LEAVES,
            CommonBlockProperties.PERSISTENT_BIT,
            CommonBlockProperties.UPDATE_BIT
        )

    }
}