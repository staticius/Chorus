package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockPolishedDioriteStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStairs(blockstate) {
    override val name: String
        get() = "Polished Diorite Stairs"

    override val hardness: Double
        get() = 1.5

    override val resistance: Double
        get() = 30.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.POLISHED_DIORITE_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
            get() = Companion.field
    }
}