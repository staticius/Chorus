package org.chorus.inventory.request

import org.chorus.Player
import org.chorus.inventory.CraftingTableInventory
import org.chorus.inventory.Inventory
import org.chorus.inventory.InventoryHolder
import org.chorus.network.protocol.types.itemstack.ContainerSlotType

object NetworkMapping {
    fun getInventory(player: Player, containerSlotType: ContainerSlotType): Inventory {
        return when (containerSlotType) {
            ContainerSlotType.HORSE_EQUIP -> {
                val riding = player.getRiding()
                if (riding is InventoryHolder) {
                    riding.inventory
                } else {
                    throw IllegalArgumentException("Can't handle horse inventory: ${containerSlotType.name.uppercase()} when an ItemStackRequest is received!")
                }
            }

            ContainerSlotType.CREATED_OUTPUT -> player.creativeOutputInventory
            ContainerSlotType.CURSOR -> player.cursorInventory
            ContainerSlotType.INVENTORY, ContainerSlotType.HOTBAR, ContainerSlotType.HOTBAR_AND_INVENTORY -> player.inventory
            ContainerSlotType.ARMOR -> player.inventory.armorInventory!!
            ContainerSlotType.OFFHAND -> player.offhandInventory!!
            ContainerSlotType.BEACON_PAYMENT, ContainerSlotType.TRADE2_INGREDIENT_1, ContainerSlotType.TRADE2_INGREDIENT_2, ContainerSlotType.TRADE2_RESULT, ContainerSlotType.LOOM_DYE, ContainerSlotType.LOOM_MATERIAL, ContainerSlotType.LOOM_INPUT, ContainerSlotType.LOOM_RESULT, ContainerSlotType.BARREL, ContainerSlotType.BREWING_RESULT, ContainerSlotType.BREWING_FUEL, ContainerSlotType.BREWING_INPUT, ContainerSlotType.FURNACE_FUEL, ContainerSlotType.FURNACE_INGREDIENT, ContainerSlotType.FURNACE_RESULT, ContainerSlotType.SMOKER_INGREDIENT, ContainerSlotType.BLAST_FURNACE_INGREDIENT, ContainerSlotType.ENCHANTING_INPUT, ContainerSlotType.ENCHANTING_MATERIAL, ContainerSlotType.SMITHING_TABLE_TEMPLATE, ContainerSlotType.SMITHING_TABLE_INPUT, ContainerSlotType.SMITHING_TABLE_MATERIAL, ContainerSlotType.SMITHING_TABLE_RESULT, ContainerSlotType.ANVIL_INPUT, ContainerSlotType.ANVIL_MATERIAL, ContainerSlotType.ANVIL_RESULT, ContainerSlotType.STONECUTTER_INPUT, ContainerSlotType.STONECUTTER_RESULT, ContainerSlotType.GRINDSTONE_ADDITIONAL, ContainerSlotType.GRINDSTONE_INPUT, ContainerSlotType.GRINDSTONE_RESULT, ContainerSlotType.CARTOGRAPHY_INPUT, ContainerSlotType.CARTOGRAPHY_ADDITIONAL, ContainerSlotType.CARTOGRAPHY_RESULT, ContainerSlotType.LEVEL_ENTITY, ContainerSlotType.SHULKER_BOX -> {
                if (player.enderChestOpen) {
                    player.enderChestInventory!!
                } else if (player.topWindow.isPresent) {
                    player.topWindow.get()
                } else {
                    throw IllegalArgumentException("Can't handle inventory: ${containerSlotType.name.uppercase()} when an ItemStackRequest is received!")
                }
            }

            ContainerSlotType.CRAFTING_INPUT -> {
                if (player.topWindow.isPresent && player.topWindow.get() is CraftingTableInventory) {
                    player.topWindow.get()
                } else {
                    player.craftingGrid
                }
            }

            else -> {
                throw IllegalArgumentException("Can't handle containerSlotType: ${containerSlotType.name.uppercase()} when an ItemStackRequest is received!")
            }
        }
    }
}
