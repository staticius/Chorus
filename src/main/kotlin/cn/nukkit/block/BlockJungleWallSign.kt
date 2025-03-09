package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

class BlockJungleWallSign @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockWallSign(blockState) {
    override fun getWallSignId(): String {
        return BlockID.Companion.JUNGLE_WALL_SIGN
    }

    override fun getStandingSignId(): String {
        return BlockID.Companion.JUNGLE_STANDING_SIGN
    }

    override val name: String
        get() = "Jungle Wall Sign"

    override fun toItem(): Item? {
        return ItemJungleSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.Companion.JUNGLE_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}
