package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemJungleSign

class BlockJungleWallSign @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockWallSign(blockState) {
    override fun getWallSignId(): String {
        return BlockID.JUNGLE_WALL_SIGN
    }

    override fun getStandingSignId(): String {
        return BlockID.JUNGLE_STANDING_SIGN
    }

    override val name: String
        get() = "Jungle Wall Sign"

    override fun toItem(): Item {
        return ItemJungleSign()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.JUNGLE_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)
    }
}
