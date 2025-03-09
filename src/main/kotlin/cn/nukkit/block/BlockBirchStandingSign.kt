package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

class BlockBirchStandingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStandingSign(blockstate) {
    override fun getStandingSignId(): String {
        return Companion.properties.identifier
    }

    override fun getWallSignId(): String {
        return BlockBirchWallSign.Companion.PROPERTIES.getIdentifier()
    }

    override fun toItem(): Item? {
        return ItemBirchSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BIRCH_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)
            get() = Companion.field
    }
}