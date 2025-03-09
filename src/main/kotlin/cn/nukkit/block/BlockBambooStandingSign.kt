package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

class BlockBambooStandingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStandingSign(blockstate) {
    override fun getWallSignId(): String {
        return BlockBambooWallSign.Companion.PROPERTIES.getIdentifier()
    }

    override fun toItem(): Item? {
        return ItemBambooSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BAMBOO_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)
            get() = Companion.field
    }
}