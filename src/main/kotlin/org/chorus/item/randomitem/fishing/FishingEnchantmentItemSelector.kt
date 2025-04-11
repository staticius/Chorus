package org.chorus.item.randomitem.fishing

import org.chorus.item.*
import org.chorus.item.enchantment.*
import org.chorus.item.randomitem.*

class FishingEnchantmentItemSelector : EnchantmentItemSelector {
    constructor(id: String, parent: Selector?) : this(id, 0, parent)

    constructor(id: String, meta: Int, parent: Selector?) : super(id, meta, 1, parent)

    constructor(id: String, meta: Int, count: Int, parent: Selector?) : super(Item.get(id, meta, count), parent)

    override fun getSupportEnchantments(item: Item): List<Enchantment> {
        val enchantments = ArrayList<Enchantment>()
        for (enchantment in Enchantment.Companion.registeredEnchantments) {
            if (enchantment.isFishable && (item.id == ItemID.Companion.ENCHANTED_BOOK || enchantment.canEnchant(item))) {
                enchantments.add(enchantment)
            }
        }
        return enchantments
    }
}
