package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemPaleOakSign

class BlockPaleOakWallSign @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockWallSign(blockState) {
    override fun getWallSignId(): String {
        return BlockID.PALE_OAK_WALL_SIGN
    }

    override fun getStandingSignId(): String {
        return BlockID.PALE_OAK_STANDING_SIGN
    }

    override val name: String
        get() = "Pale Oak Wall Sign"

    override fun toItem(): Item {
        return ItemPaleOakSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PALE_OAK_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)

    }
}
