package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemMangroveSign

class BlockMangroveStandingSign @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockStandingSign(blockstate) {
    override fun getWallSignId(): String {
        return BlockMangroveWallSign.properties.identifier
    }

    override fun toItem(): Item {
        return ItemMangroveSign()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.MANGROVE_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)

    }
}