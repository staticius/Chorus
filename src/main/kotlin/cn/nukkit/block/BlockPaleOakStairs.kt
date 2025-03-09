package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockPaleOakStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStairsWood(blockstate) {
    override val name: String
        get() = "Pale Oak Wood Stairs"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.PALE_OAK_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
            get() = Companion.field
    }
}