package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

class BlockAcaciaStandingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStandingSign(blockstate) {
    override fun getStandingSignId(): String {
        return Companion.properties.identifier
    }

    override fun getWallSignId(): String {
        return BlockAcaciaWallSign.Companion.PROPERTIES.getIdentifier()
    }

    override fun toItem(): Item? {
        return ItemAcaciaSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(ACACIA_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)
            get() = Companion.field
    }
}