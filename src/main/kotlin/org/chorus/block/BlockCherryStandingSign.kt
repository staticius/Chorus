package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemCherrySign

class BlockCherryStandingSign @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockStandingSign(blockState) {
    override fun getWallSignId(): String {
        return BlockCherryWallSign.properties.identifier
    }

    override fun toItem(): Item {
        return ItemCherrySign()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHERRY_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)
    }
}