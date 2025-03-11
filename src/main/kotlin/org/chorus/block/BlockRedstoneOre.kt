package org.chorus.block

import org.chorus.item.Item
import org.chorus.item.Item.Companion.get
import org.chorus.item.ItemTool
import org.chorus.item.enchantment.Enchantment
import org.chorus.level.Level
import java.util.*
import java.util.concurrent.ThreadLocalRandom

open class BlockRedstoneOre @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockOre(blockstate) {
    override val toolTier: Int
        get() = ItemTool.TIER_IRON

    override val name: String
        get() = "Redstone Ore"

    override fun getDrops(item: Item): Array<Item?>? {
        if (item.isPickaxe && item.tier >= toolTier) {
            var count = Random().nextInt(2) + 4

            val fortune = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING)
            if (fortune != null && fortune.level >= 1) {
                count += Random().nextInt(fortune.level + 1)
            }

            return arrayOf<Item?>(
                get(Item.REDSTONE, 0, count)
            )
        } else {
            return Item.EMPTY_ARRAY
        }
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_TOUCH) { //type == Level.BLOCK_UPDATE_NORMAL ||
            level.setBlock(this.position, litBlock, true, true)
            return Level.BLOCK_UPDATE_WEAK
        }

        return 0
    }

    override val rawMaterial: String?
        get() = ItemID.REDSTONE

    open val litBlock: Block
        get() = BlockLitRedstoneOre()

    open val unlitBlock: Block
        get() = BlockRedstoneOre()

    override val dropExp: Int
        get() = ThreadLocalRandom.current().nextInt(1, 6)

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.REDSTONE_ORE)

    }
}