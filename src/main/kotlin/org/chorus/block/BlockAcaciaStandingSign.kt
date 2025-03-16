package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemAcaciaSign

class BlockAcaciaStandingSign @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockStandingSign(blockstate) {
    override fun getStandingSignId(): String {
        return Companion.properties.identifier
    }

    override fun getWallSignId(): String {
        return BlockAcaciaWallSign.properties.identifier
    }

    override val properties: BlockProperties
        get() = Companion.properties

    override fun toItem(): Item {
        return ItemAcaciaSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.ACACIA_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)

    }
}