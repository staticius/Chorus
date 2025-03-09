package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType
import cn.nukkit.item.Item

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
            get() = Companion.field
    }
}