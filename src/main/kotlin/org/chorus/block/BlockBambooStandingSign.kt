package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemBambooSign

class BlockBambooStandingSign @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockStandingSign(blockstate) {
    override fun getWallSignId(): String {
        return BlockBambooWallSign.properties.identifier
    }

    override fun toItem(): Item {
        return ItemBambooSign()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.BAMBOO_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)
    }
}