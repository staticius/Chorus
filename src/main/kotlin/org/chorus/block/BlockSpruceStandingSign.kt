package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item

class BlockSpruceStandingSign @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockStandingSign(blockstate) {
    override val wallSignId: String
        get() = BlockSpruceWallSign.Companion.PROPERTIES.getIdentifier()

    override fun toItem(): Item? {
        return ItemSpruceSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SPRUCE_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)

    }
}