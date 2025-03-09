package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*
import cn.nukkit.item.Item.Companion.get
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.utils.random.NukkitRandom

class BlockAmethystCluster @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockAmethystBud(blockState) {
    override val namePrefix: String
        get() = "Cluster"

    override val lightLevel: Int
        get() = 5

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun getDrops(item: Item): Array<Item?>? {
        if (item.isPickaxe) {
            val fortuneLvl = item.getEnchantmentLevel(Enchantment.ID_FORTUNE_DIGGING)
            when (fortuneLvl) {
                1 -> return if (RANDOM.nextInt(3) == 0) {
                    arrayOf(get(ItemID.AMETHYST_SHARD, 0, 8))
                } else {
                    arrayOf(get(ItemID.AMETHYST_SHARD, 0, 4))
                }

                2 -> {
                    val bound = RANDOM.nextInt(4)
                    return if (bound == 0) {
                        arrayOf(get(ItemID.AMETHYST_SHARD, 0, 12))
                    } else if (bound == 1) {
                        arrayOf(get(ItemID.AMETHYST_SHARD, 0, 8))
                    } else {
                        arrayOf(get(ItemID.AMETHYST_SHARD, 0, 4))
                    }
                }

                3 -> {
                    val bound2 = RANDOM.nextInt(5)
                    if (bound2 == 0) {
                        return arrayOf(get(ItemID.AMETHYST_SHARD, 0, 16))
                    } else if (bound2 == 1) {
                        return arrayOf(get(ItemID.AMETHYST_SHARD, 0, 12))
                    } else if (bound2 == 2) {
                        return arrayOf(get(ItemID.AMETHYST_SHARD, 0, 8))
                    }
                    return arrayOf(get(ItemID.AMETHYST_SHARD, 0, 4))
                }

                else -> return arrayOf(get(ItemID.AMETHYST_SHARD, 0, 4))
            }
        } else {
            return arrayOf(get(ItemID.AMETHYST_SHARD, 0, 2))
        }
    }

    companion object {
        val properties: BlockProperties = BlockProperties(AMETHYST_CLUSTER, CommonBlockProperties.MINECRAFT_BLOCK_FACE)
            get() = Companion.field

        private val RANDOM = NukkitRandom()
    }
}
