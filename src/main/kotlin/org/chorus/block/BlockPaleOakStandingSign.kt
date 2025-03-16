package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.*

class BlockPaleOakStandingSign @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockStandingSign(blockstate) {
    override fun getStandingSignId(): String {
        return Companion.properties.identifier
    }

    override fun getWallSignId(): String {
        return BlockPaleOakWallSign.Companion.PROPERTIES.getIdentifier()
    }

    override fun toItem(): Item {
        return ItemPaleOakSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PALE_OAK_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)

    }
}