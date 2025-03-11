package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.*

class BlockBirchStandingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStandingSign(blockstate) {
    override fun getStandingSignId(): String {
        return Companion.properties.identifier
    }

    override fun getWallSignId(): String {
        return BlockBirchWallSign.Companion.PROPERTIES.getIdentifier()
    }

    override fun toItem(): Item? {
        return ItemBirchSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.BIRCH_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)

    }
}