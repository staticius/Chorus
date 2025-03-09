package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

class BlockDarkoakWallSign @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockWallSign(blockState) {
    override fun getWallSignId(): String {
        return DARKOAK_WALL_SIGN
    }

    override fun getStandingSignId(): String {
        return DARKOAK_STANDING_SIGN
    }

    override fun toItem(): Item? {
        return ItemDarkOakSign()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(DARKOAK_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}
