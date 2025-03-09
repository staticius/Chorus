package org.chorus.item.enchantment

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EnchantmentWaterWorker :
    Enchantment(Enchantment.Companion.ID_WATER_WORKER, "waterWorker", Rarity.RARE, EnchantmentType.ARMOR_HEAD) {
    override fun getMinEnchantAbility(level: Int): Int {
        return 1
    }

    override fun getMaxEnchantAbility(level: Int): Int {
        return this.getMinEnchantAbility(level) + 40
    }
}
