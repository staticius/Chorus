package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.Item
import cn.nukkit.item.ItemTool

open class BlockStoneButton @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockButton(blockstate) {
    override val name: String
        get() = "Stone Button"

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return super.getDrops(item)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.STONE_BUTTON,
            CommonBlockProperties.BUTTON_PRESSED_BIT,
            CommonBlockProperties.FACING_DIRECTION
        )
            get() = Companion.field
    }
}