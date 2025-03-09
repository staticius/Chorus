package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType
import org.chorus.item.Item

class BlockOakLeaves(blockstate: BlockState?) : BlockLeaves(blockstate) {
    override val type: WoodType
        get() = WoodType.OAK

    override fun toSapling(): Item {
        return Item.get(BlockID.OAK_SAPLING)
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.OAK_LEAVES, CommonBlockProperties.PERSISTENT_BIT, CommonBlockProperties.UPDATE_BIT)
            get() = Companion.field
    }
}