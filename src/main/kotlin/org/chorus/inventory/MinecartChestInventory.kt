package org.chorus.inventory

import org.chorus.entity.item.EntityChestMinecart


class MinecartChestInventory(minecart: EntityChestMinecart) :
    ContainerInventory(minecart, InventoryType.MINECART_CHEST, 27) {
    override fun canCauseVibration(): Boolean {
        return true
    }
}
