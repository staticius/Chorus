package cn.nukkit.level.generator.terra.delegate

import cn.nukkit.item.Item
import com.dfsek.terra.api.inventory.item.Enchantment
import com.dfsek.terra.api.inventory.item.ItemMeta
import kotlin.collections.HashMap
import kotlin.collections.Map
import kotlin.collections.set

@JvmRecord
data class PNXItemMeta(val innerItem: Item) : ItemMeta {
    override fun addEnchantment(enchantment: Enchantment, i: Int) {
        val enc = (enchantment as PNXEnchantmentDelegate).innerEnchantment
        enc.setLevel(i, false)
        innerItem.addEnchantment(enc)
    }

    override fun getEnchantments(): Map<Enchantment, Int> {
        val map = HashMap<Enchantment, Int>()
        for (each in innerItem.enchantments) {
            map[PNXEnchantmentDelegate(each)] = each.level
        }
        return map
    }

    override fun getHandle(): Item {
        return innerItem
    }
}
