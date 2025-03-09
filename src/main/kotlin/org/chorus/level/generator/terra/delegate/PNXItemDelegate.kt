package org.chorus.level.generator.terra.delegate

import org.chorus.item.Item
import org.chorus.item.Item.setCount
import com.dfsek.terra.api.inventory.ItemStack

@JvmRecord
data class PNXItemDelegate(val innerItem: Item) : com.dfsek.terra.api.inventory.Item {
    override fun newItemStack(i: Int): ItemStack {
        val tmp = innerItem.clone()
        tmp.setCount(i)
        return PNXItemStack(tmp)
    }

    override fun getMaxDurability(): Double {
        return innerItem.maxDurability.toDouble()
    }

    override fun getHandle(): Item {
        return innerItem
    }
}
