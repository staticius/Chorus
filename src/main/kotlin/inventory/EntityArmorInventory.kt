package org.chorus_oss.chorus.inventory

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.entity.Entity
import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.network.protocol.InventoryContentPacket
import org.chorus_oss.chorus.network.protocol.InventorySlotPacket
import org.chorus_oss.chorus.network.protocol.MobArmorEquipmentPacket
import org.chorus_oss.chorus.network.protocol.types.inventory.FullContainerName
import org.chorus_oss.chorus.network.protocol.types.itemstack.ContainerSlotType

class EntityArmorInventory(holder: InventoryHolder) : BaseInventory(holder, InventoryType.ARMOR, 5) {
    val entity: Entity = holder as Entity


    override val size: Int
        get() = 5


    val helmet: Item
        get() = this.getItem(SLOT_HEAD)


    val chestplate: Item
        get() = this.getItem(SLOT_CHEST)


    val leggings: Item
        get() = this.getItem(SLOT_LEGS)


    val boots: Item
        get() = this.getItem(SLOT_FEET)

    val body: Item
        get() = this.getItem(SLOT_BODY)


    fun setHelmet(item: Item?): Boolean {
        return this.setItem(SLOT_HEAD, item!!)
    }


    fun setChestplate(item: Item?): Boolean {
        return this.setItem(SLOT_CHEST, item!!)
    }


    fun setLeggings(item: Item?): Boolean {
        return this.setItem(SLOT_LEGS, item!!)
    }


    fun setBoots(item: Item?): Boolean {
        return this.setItem(SLOT_FEET, item!!)
    }

    fun setBody(item: Item): Boolean {
        return this.setItem(SLOT_BODY, item)
    }

    override fun sendSlot(index: Int, vararg players: Player) {
        for (player in players) {
            this.sendSlot(index, player)
        }
    }

    override fun sendSlot(index: Int, player: Player) {
        if (player === this.holder) {
            val inventorySlotPacket = InventorySlotPacket()
            val id = player.getWindowId(this)
            inventorySlotPacket.inventoryId = id
            inventorySlotPacket.slot = index
            inventorySlotPacket.item = this.getItem(index)
            inventorySlotPacket.fullContainerName = FullContainerName(
                this.getSlotType(index),
                id
            )
            player.dataPacket(inventorySlotPacket)
        } else {
            val mobArmorEquipmentPacket = MobArmorEquipmentPacket()
            mobArmorEquipmentPacket.eid = entity.getRuntimeID()
            mobArmorEquipmentPacket.slots = arrayOf(
                this.helmet,
                chestplate,
                leggings,
                boots
            )
            mobArmorEquipmentPacket.body = this.body

            player.dataPacket(mobArmorEquipmentPacket)
        }
    }

    override fun sendContents(vararg players: Player) {
        for (player in players) {
            this.sendContents(player)
        }
    }

    override fun sendContents(player: Player) {
        if (player === this.holder) {
            val inventoryContentPacket = InventoryContentPacket()
            val id = player.getWindowId(this)
            inventoryContentPacket.inventoryId = id
            inventoryContentPacket.slots = arrayOf(
                this.helmet,
                chestplate,
                leggings,
                boots
            )
            inventoryContentPacket.fullContainerName = FullContainerName(
                ContainerSlotType.ARMOR,
                id
            )
            player.dataPacket(inventoryContentPacket)
        } else {
            val mobArmorEquipmentPacket = MobArmorEquipmentPacket()
            mobArmorEquipmentPacket.eid = entity.getRuntimeID()
            mobArmorEquipmentPacket.slots = arrayOf(
                this.helmet,
                chestplate,
                leggings,
                boots
            )
            mobArmorEquipmentPacket.body = this.body

            player.dataPacket(mobArmorEquipmentPacket)
        }
    }

    override val viewers: MutableSet<Player> = super.viewers
        get() {
            val viewers: MutableSet<Player> = HashSet(field)
            viewers.addAll(entity.viewers.values)
            return viewers
        }

    companion object {
        const val SLOT_HEAD: Int = 0
        const val SLOT_CHEST: Int = 1
        const val SLOT_LEGS: Int = 2
        const val SLOT_FEET: Int = 3
        const val SLOT_BODY: Int = 4
    }
}
