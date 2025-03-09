package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.Item

class BlockSpruceStandingSign @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockStandingSign(blockstate) {
    override val wallSignId: String
        get() = BlockSpruceWallSign.Companion.PROPERTIES.getIdentifier()

    override fun toItem(): Item? {
        return ItemSpruceSign()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SPRUCE_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION)
            get() = Companion.field
    }
}