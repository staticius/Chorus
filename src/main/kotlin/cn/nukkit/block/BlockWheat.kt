package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.Item
import cn.nukkit.item.Item.Companion.get
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.math.BlockFace
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.min

/**
 * @author xtypr
 * @since 2015/12/2
 */
class BlockWheat @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockCrops(blockstate) {
    override val name: String
        get() = "Wheat Block"

    override fun getDrops(item: Item): Array<Item?>? {
        // https://minecraft.wiki/w/Fortune#Seeds
        if (!isFullyGrown) {
            return arrayOf<Item?>(ItemWheatSeeds())
        }

        val random = ThreadLocalRandom.current()
        var count = 0
        val attempts = (3 + min(
            0.0,
            item.getEnchantmentLevel(Enchantment.ID_FORTUNE_DIGGING).toDouble()
        )).toInt()
        // Fortune increases the number of tests for the distribution, and thus the maximum number of drops, by 1 per level
        for (i in 0..<attempts) {
            // The binomial distribution in the default case is created by rolling three times (n=3) with a drop probability of 57%
            if (random.nextInt(7) < 4) { // 4/7, 0.57142857142857142857142857142857
                count++
            }
        }

        return if (count > 0) {
            arrayOf<Item?>(toItem(), get(ItemID.WHEAT_SEEDS, 0, count))
        } else {
            arrayOf<Item?>(toItem())
        }
    }

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        if (item.id == BlockID.WHEAT) return false
        return super.place(item, block, target, face, fx, fy, fz, player)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WHEAT, CommonBlockProperties.GROWTH)
            get() = Companion.field
    }
}
