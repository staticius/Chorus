package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.Item

class BlockSpruceWallSign @JvmOverloads constructor(blockState: BlockState? = Companion.properties.getDefaultState()) :
    BlockWallSign(blockState) {
    override val name: String
        get() = "Spruce Wall Sign"

    override fun getWallSignId(): String {
        return BlockID.SPRUCE_WALL_SIGN
    }

    override fun getStandingSignId(): String {
        return BlockID.SPRUCE_STANDING_SIGN
    }

    override fun toItem(): Item? {
        return ItemSpruceSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SPRUCE_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}
