package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType
import org.chorus.item.Item

class BlockSpruceLeaves(blockstate: BlockState?) : BlockLeaves(blockstate) {
    override val type: WoodType
        get() = WoodType.SPRUCE

    override fun toSapling(): Item {
        return Item.get(BlockID.SPRUCE_SAPLING)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.SPRUCE_LEAVES,
            CommonBlockProperties.PERSISTENT_BIT,
            CommonBlockProperties.UPDATE_BIT
        )

    }
}