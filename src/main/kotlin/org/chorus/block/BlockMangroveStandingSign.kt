package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item

class BlockMangroveStandingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStandingSign(blockstate) {
    override fun getWallSignId(): String {
        return BlockMangroveWallSign.Companion.PROPERTIES.getIdentifier()
    }

    override fun toItem(): Item? {
        return ItemMangroveSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.MANGROVE_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)

    }
}