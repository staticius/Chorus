package cn.nukkit.level.generator.terra.handles

import cn.nukkit.item.Item.Companion.get
import cn.nukkit.item.enchantment.Enchantment.Companion.registeredEnchantments
import cn.nukkit.level.generator.terra.PNXAdapter
import cn.nukkit.level.generator.terra.delegate.PNXEnchantmentDelegate
import com.dfsek.terra.api.handle.ItemHandle
import com.dfsek.terra.api.inventory.Item
import com.dfsek.terra.api.inventory.item.Enchantment
import java.util.stream.Collectors

class PNXItemHandle : ItemHandle {
    override fun createItem(s: String): Item {
        return PNXAdapter.adapt(get(s))
    }

    override fun getEnchantment(s: String): Enchantment {
        var s = s
        if (s.startsWith("minecraft:")) s = s.substring(10)
        return PNXEnchantmentDelegate(cn.nukkit.item.enchantment.Enchantment.getEnchantment(s)!!)
    }

    override fun getEnchantments(): Set<Enchantment> {
        return registeredEnchantments.stream()
            .map { innerEnchantment: cn.nukkit.item.enchantment.Enchantment ->
                PNXEnchantmentDelegate(
                    innerEnchantment
                )
            }
            .collect(Collectors.toSet())
    }
}
