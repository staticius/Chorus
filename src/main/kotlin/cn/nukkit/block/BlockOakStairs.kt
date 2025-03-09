package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockOakStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStairsWood(blockstate) {
    override val name: String
        get() = "Oak Wood Stairs"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.OAK_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
            get() = Companion.field
    }
}