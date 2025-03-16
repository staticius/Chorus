package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.Item.Companion.get
import org.chorus.item.ItemID
import org.chorus.item.ItemTool
import org.chorus.item.enchantment.Enchantment
import org.chorus.utils.ChorusRandom

class BlockAmethystCluster @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockAmethystBud(blockState) {
    override val namePrefix: String
        get() = "Cluster"

    override val lightLevel: Int
        get() = 5

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun getDrops(item: Item): Array<Item> {
        if (item.isPickaxe) {
            val fortuneLvl = item.getEnchantmentLevel(Enchantment.ID_FORTUNE_DIGGING)
            when (fortuneLvl) {
                1 -> {
                    val bound = RANDOM.nextInt(3)
                    return when (bound) {
                        0 -> arrayOf(get(ItemID.AMETHYST_SHARD, 0, 8))
                        else -> arrayOf(get(ItemID.AMETHYST_SHARD, 0, 4))
                    }
                }

                2 -> {
                    val bound = RANDOM.nextInt(4)
                    return when (bound) {
                        0 -> arrayOf(get(ItemID.AMETHYST_SHARD, 0, 12))
                        1 -> arrayOf(get(ItemID.AMETHYST_SHARD, 0, 8))
                        else -> arrayOf(get(ItemID.AMETHYST_SHARD, 0, 4))
                    }
                }

                3 -> {
                    val bound2 = RANDOM.nextInt(5)
                    return when (bound2) {
                        0 -> arrayOf(get(ItemID.AMETHYST_SHARD, 0, 16))
                        1 -> arrayOf(get(ItemID.AMETHYST_SHARD, 0, 12))
                        2 -> arrayOf(get(ItemID.AMETHYST_SHARD, 0, 8))
                        else -> arrayOf(get(ItemID.AMETHYST_SHARD, 0, 4))
                    }
                }

                else -> return arrayOf(get(ItemID.AMETHYST_SHARD, 0, 4))
            }
        } else {
            return arrayOf(get(ItemID.AMETHYST_SHARD, 0, 2))
        }
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.AMETHYST_CLUSTER, CommonBlockProperties.MINECRAFT_BLOCK_FACE)

        private val RANDOM = ChorusRandom()
    }
}
