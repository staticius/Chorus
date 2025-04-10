package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemJungleSign

class BlockJungleStandingSign @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockStandingSign(blockstate) {
    override fun getWallSignId(): String {
        return BlockJungleWallSign.properties.identifier
    }

    override fun toItem(): Item {
        return ItemJungleSign()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.JUNGLE_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)

    }
}