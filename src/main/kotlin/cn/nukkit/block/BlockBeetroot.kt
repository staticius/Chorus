package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*
import cn.nukkit.item.Item.Companion.get
import cn.nukkit.item.enchantment.Enchantment
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.min

/**
 * @author xtypr
 * @since 2015/11/22
 */
class BlockBeetroot @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCrops(blockstate) {
    override val name: String
        get() = "Beetroot Block"

    override fun toItem(): Item? {
        return ItemBeetrootSeeds()
    }

    override fun getDrops(item: Item): Array<Item?>? {
        if (!isFullyGrown) {
            return arrayOf(Item.get(ItemID.BEETROOT_SEEDS))
        }

        var seeds = 1
        val attempts = (3 + min(
            0.0,
            item.getEnchantmentLevel(Enchantment.ID_FORTUNE_DIGGING).toDouble()
        )).toInt()
        val random = ThreadLocalRandom.current()
        for (i in 0..<attempts) {
            if (random.nextInt(7) < 4) { // 4/7, 0.57142857142857142857142857142857
                seeds++
            }
        }

        return arrayOf(
            Item.get(BEETROOT),
            get(ItemID.BEETROOT_SEEDS, 0, seeds)
        )
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BEETROOT, CommonBlockProperties.GROWTH)
            get() = Companion.field
    }
}
