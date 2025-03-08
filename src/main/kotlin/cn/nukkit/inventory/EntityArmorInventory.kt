package cn.nukkit.inventory

import cn.nukkit.Player
import cn.nukkit.entity.Entity
import cn.nukkit.item.Item
import cn.nukkit.network.protocol.InventoryContentPacket
import cn.nukkit.network.protocol.InventorySlotPacket
import cn.nukkit.network.protocol.MobArmorEquipmentPacket
import cn.nukkit.network.protocol.types.inventory.FullContainerName
import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType

class EntityArmorInventory(holder: InventoryHolder) : BaseInventory(holder, InventoryType.ARMOR, 4) {
    val entity: Entity = holder as Entity


    override val size: Int
        get() = 4


    val helmet: Item
        get() = this.getItem(SLOT_HEAD)


    val chestplate: Item
        get() = this.getItem(SLOT_CHEST)


    val leggings: Item
        get() = this.getItem(SLOT_LEGS)


    val boots: Item
        get() = this.getItem(SLOT_FEET)


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

    override fun sendSlot(index: Int, vararg players: Player) {
        for (player in players) {
            this.sendSlot(index, player)
        }
    }

    override fun sendSlot(index: Int, player: Player) {
        val mobArmorEquipmentPacket = MobArmorEquipmentPacket()
        mobArmorEquipmentPacket.eid = entity.getId()
        mobArmorEquipmentPacket.slots = arrayOf(
            this.helmet,
            chestplate,
            leggings,
            boots
        )

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
            player.dataPacket(mobArmorEquipmentPacket)
        }
    }

    override fun sendContents(vararg players: Player) {
        for (player in players) {
            this.sendContents(player)
        }
    }

    override fun sendContents(player: Player) {
        val mobArmorEquipmentPacket = MobArmorEquipmentPacket()
        mobArmorEquipmentPacket.eid = entity.getId()
        mobArmorEquipmentPacket.slots = arrayOf(
            this.helmet,
            chestplate,
            leggings,
            boots
        )

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
            player.dataPacket(mobArmorEquipmentPacket)
        }
    }

    override fun getViewers(): Set<Player?> {
        val viewers: MutableSet<Player?> = HashSet(this.viewers)
        viewers.addAll(entity.getViewers().values)
        return viewers
    }

    companion object {
        const val SLOT_HEAD: Int = 0
        const val SLOT_CHEST: Int = 1
        const val SLOT_LEGS: Int = 2
        const val SLOT_FEET: Int = 3
    }
}
