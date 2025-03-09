package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

class BlockPaleOakWallSign @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockWallSign(blockState) {
    override fun getWallSignId(): String {
        return BlockID.PALE_OAK_WALL_SIGN
    }

    override fun getStandingSignId(): String {
        return BlockID.PALE_OAK_STANDING_SIGN
    }

    override val name: String
        get() = "Pale Oak Wall Sign"

    override fun toItem(): Item? {
        return ItemPaleOakSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PALE_OAK_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}
