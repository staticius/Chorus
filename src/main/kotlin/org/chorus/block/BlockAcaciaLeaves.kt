package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType
import org.chorus.item.*

class BlockAcaciaLeaves(blockstate: BlockState?) : BlockLeaves(blockstate) {
    override fun getType(): WoodType {
        return WoodType.ACACIA
    }

    override fun toSapling(): Item {
        return Item.get(ACACIA_SAPLING)
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(ACACIA_LEAVES, CommonBlockProperties.PERSISTENT_BIT, CommonBlockProperties.UPDATE_BIT)
            get() = Companion.field
    }
}