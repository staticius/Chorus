package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

class BlockDarkoakStandingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStandingSign(blockstate) {
    override fun getWallSignId(): String {
        return BlockDarkoakWallSign.Companion.PROPERTIES.getIdentifier()
    }

    override fun toItem(): Item? {
        return ItemDarkOakSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(DARKOAK_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)
            get() = Companion.field
    }
}