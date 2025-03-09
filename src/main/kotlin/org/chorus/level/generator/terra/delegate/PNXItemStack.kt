package org.chorus.level.generator.terra.delegate

import cn.nukkit.item.Item
import com.dfsek.terra.api.inventory.ItemStack
import com.dfsek.terra.api.inventory.item.Damageable
import com.dfsek.terra.api.inventory.item.ItemMeta

@JvmRecord
data class PNXItemStack(val innerItem: Item) : ItemStack, Damageable {
    override fun getAmount(): Int {
        return innerItem.getCount()
    }

    override fun setAmount(i: Int) {
        innerItem.setCount(i)
    }

    override fun getType(): com.dfsek.terra.api.inventory.Item {
        return PNXItemDelegate(innerItem)
    }

    override fun getItemMeta(): ItemMeta {
        return PNXItemMeta(innerItem)
    }

    // TODO: 2022/2/14 确认setItemMeta的用途，当前实现可能造成附魔混乱
    override fun setItemMeta(itemMeta: ItemMeta) {
        val tmp = itemMeta as PNXItemMeta
        innerItem.addEnchantment(*tmp.innerItem.enchantments)
    }

    override fun getHandle(): Item {
        return innerItem
    }

    override fun isDamageable(): Boolean {
        return true
    }

    override fun getDamage(): Int {
        return innerItem.damage
    }

    override fun setDamage(i: Int) {
        innerItem.damage = i
    }

    override fun hasDamage(): Boolean {
        return innerItem.damage != 0
    }
}
