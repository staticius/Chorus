package org.chorus.block

import org.chorus.item.Item
import org.chorus.item.Item.Companion.get
import org.chorus.item.ItemID
import org.chorus.item.ItemTool
import org.chorus.item.enchantment.Enchantment
import org.chorus.utils.random.ChorusRandom.nextInt

class BlockNetherGoldOre @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockGoldOre(blockstate) {
    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val name: String
        get() = "Nether Gold Ore"

    override fun getDrops(item: Item): Array<Item?>? {
        if (!item.isPickaxe || item.tier < ItemTool.TIER_WOODEN) {
            return Item.EMPTY_ARRAY
        }

        val enchantment = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING)
        var fortune = 0
        if (enchantment != null) {
            fortune = enchantment.level
        }

        val nukkitRandom: NukkitRandom = NukkitRandom()
        var count: Int = nukkitRandom.nextInt(2, 6)
        when (fortune) {
            0 -> {
                // Does nothing
            }

            1 -> {
                if (nukkitRandom.nextInt(0, 2) == 0) {
                    count *= 2
                }
            }

            2 -> {
                if (nukkitRandom.nextInt(0, 1) == 0) {
                    count *= nukkitRandom.nextInt(2, 3)
                }
            }

            else -> {
                if (nukkitRandom.nextInt(0, 4) < 3) {
                    count *= nukkitRandom.nextInt(2, 4)
                }
            }
        }

        return arrayOf(get(ItemID.GOLD_NUGGET, 0, count))
    }

    override fun getRawMaterial(): String? {
        return ItemID.GOLD_NUGGET
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.NETHER_GOLD_ORE)

    }
}