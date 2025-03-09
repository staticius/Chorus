package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

class BlockCrimsonStandingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStandingSign(blockstate) {
    override fun getWallSignId(): String {
        return BlockCrimsonWallSign.Companion.PROPERTIES.getIdentifier()
    }

    override fun toItem(): Item? {
        return ItemCherrySign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(CRIMSON_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)
            get() = Companion.field
    }
}