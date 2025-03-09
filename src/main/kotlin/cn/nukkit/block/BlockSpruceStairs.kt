package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockSpruceStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockStairsWood(blockstate) {
    override val name: String
        get() = "Spruce Wood Stairs"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.SPRUCE_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
            get() = Companion.field
    }
}