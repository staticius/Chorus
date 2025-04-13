package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType
import org.chorus.item.Item

class BlockAcaciaLeaves(blockstate: BlockState = Companion.properties.defaultState) : BlockLeaves(blockstate) {
    override fun getType(): WoodType {
        return WoodType.ACACIA
    }

    override fun toSapling(): Item {
        return Item.get(Companion.properties.identifier)
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(
                BlockID.ACACIA_LEAVES,
                CommonBlockProperties.PERSISTENT_BIT,
                CommonBlockProperties.UPDATE_BIT
            )
    }
}