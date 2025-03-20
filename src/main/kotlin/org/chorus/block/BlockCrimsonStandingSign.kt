package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemCherrySign

class BlockCrimsonStandingSign @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockStandingSign(blockstate) {
    override fun getWallSignId(): String {
        return BlockCrimsonWallSign.properties.identifier
    }

    override fun toItem(): Item {
        return ItemCherrySign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.CRIMSON_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)

    }
}