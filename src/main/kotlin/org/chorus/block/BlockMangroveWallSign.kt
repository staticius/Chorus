package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemMangroveSign

class BlockMangroveWallSign @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockWallSign(blockState) {
    override fun getWallSignId(): String {
        return BlockID.MANGROVE_WALL_SIGN
    }

    override fun getStandingSignId(): String {
        return BlockID.MANGROVE_STANDING_SIGN
    }

    override val name: String
        get() = "Mangrove Wall Sign"

    override fun toItem(): Item {
        return ItemMangroveSign()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.MANGROVE_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)

    }
}
