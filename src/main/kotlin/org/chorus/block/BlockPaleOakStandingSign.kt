package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

class BlockPaleOakStandingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStandingSign(blockstate) {
    override fun getStandingSignId(): String {
        return Companion.properties.identifier
    }

    override fun getWallSignId(): String {
        return BlockPaleOakWallSign.Companion.PROPERTIES.getIdentifier()
    }

    override fun toItem(): Item? {
        return ItemPaleOakSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PALE_OAK_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)
            get() = Companion.field
    }
}