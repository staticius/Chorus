package org.chorus.level.generator.terra.handles

import org.chorus.item.Item.Companion.get
import org.chorus.item.enchantment.Enchantment.Companion.registeredEnchantments
import org.chorus.level.generator.terra.PNXAdapter
import org.chorus.level.generator.terra.delegate.PNXEnchantmentDelegate
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
