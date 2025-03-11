package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.*

class BlockJungleStandingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStandingSign(blockstate) {
    override fun getWallSignId(): String {
        return BlockJungleWallSign.Companion.PROPERTIES.getIdentifier()
    }

    override fun toItem(): Item? {
        return ItemJungleSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.Companion.JUNGLE_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)

    }
}