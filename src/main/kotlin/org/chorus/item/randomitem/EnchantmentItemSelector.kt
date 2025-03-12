package org.chorus.item.randomitem

import org.chorus.entity.effect.PotionType.Companion.get
import org.chorus.item.*
import org.chorus.item.enchantment.*
import org.chorus.utils.*
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * @author LT_Name
 */
open class EnchantmentItemSelector(item: Item, parent: Selector?) :
    ConstantItemSelector(item, parent) {
    constructor(id: String?, parent: Selector?) : this(id, 0, parent)

    constructor(id: String?, meta: Int, parent: Selector?) : this(id, meta, 1, parent)

    constructor(id: String?, meta: Int, count: Int, parent: Selector?) : this(get(id, meta, count), parent)

    init {
        //TODO 贴近原版附魔概率
        val enchantments = getSupportEnchantments(item)
        if (!enchantments.isEmpty()) {
            val random: Random = ThreadLocalRandom.current()
            val enchantment = enchantments[random.nextInt(enchantments.size)]
            if (random.nextDouble() < 0.3) { //减少高等级附魔概率
                enchantment.setLevel(Utils.rand(1, enchantment.maxLevel))
            }
            item.addEnchantment(enchantment)
        }
    }

    /**
     * 根据物品获得支持的附魔
     *
     * @param item 物品
     * @return 支持的附魔
     */
    open fun getSupportEnchantments(item: Item): List<Enchantment> {
        val enchantments = ArrayList<Enchantment>()
        for (enchantment in Enchantment.Companion.getRegisteredEnchantments()) {
            if (item.id == ItemID.Companion.ENCHANTED_BOOK || enchantment.canEnchant(item)) {
                enchantments.add(enchantment)
            }
        }
        return enchantments
    }
}
