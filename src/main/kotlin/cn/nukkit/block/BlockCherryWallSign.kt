package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

class BlockCherryWallSign @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockWallSign(blockState) {
    override fun getWallSignId(): String {
        return CHERRY_WALL_SIGN
    }

    override fun getStandingSignId(): String {
        return CHERRY_STANDING_SIGN
    }

    override fun toItem(): Item? {
        return ItemCherrySign()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(CHERRY_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}
