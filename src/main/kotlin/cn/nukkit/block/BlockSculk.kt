package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.item.Item
import cn.nukkit.item.ItemTool
import cn.nukkit.item.enchantment.Enchantment

class BlockSculk @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Sculk"

    override val hardness: Double
        get() = 0.2

    override val resistance: Double
        get() = 0.2

    override val toolType: Int
        get() = ItemTool.TYPE_HOE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun calculateBreakTime(item: Item, player: Player?): Double {
        return if (canHarvest(item)) {
            super.calculateBreakTime(item, player)
        } else {
            1.0
        }
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return if (item.getEnchantment(Enchantment.ID_SILK_TOUCH) != null) {
            super.getDrops(item)
        } else {
            Item.EMPTY_ARRAY
        }
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val dropExp: Int
        get() = 1

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SCULK)
            get() = Companion.field
    }
}
