package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.*

class BlockCrimsonStandingSign @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockStandingSign(blockstate) {
    override fun getWallSignId(): String {
        return BlockCrimsonWallSign.Companion.PROPERTIES.getIdentifier()
    }

    override fun toItem(): Item {
        return ItemCherrySign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.CRIMSON_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)

    }
}