package org.chorus_oss.chorus.inventory

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.entity.Entity
import org.chorus_oss.chorus.experimental.network.protocol.utils.from
import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.network.protocol.MobArmorEquipmentPacket
import org.chorus_oss.chorus.network.protocol.types.inventory.FullContainerName
import org.chorus_oss.protocol.types.item.ItemStack

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
            val id = player.getWindowId(this)
            val packet = org.chorus_oss.protocol.packets.InventorySlotPacket(
                windowID = id.toUInt(),
                slot = index.toUInt(),
                container = org.chorus_oss.protocol.types.inventory.FullContainerName.from(
                    FullContainerName(
                        this.getSlotType(index),
                        id
                    )
                ),
                storageItem = ItemStack.from(Item.AIR),
                newItem = ItemStack.from(this.getItem(index))
            )
            player.sendPacket(packet)
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
            val id = player.getWindowId(this)
            val packet = org.chorus_oss.protocol.packets.InventoryContentPacket(
                windowID = id.toUInt(),
                content = listOf(
                    this.helmet,
                    chestplate,
                    leggings,
                    boots
                ).map { ItemStack.from(it) },
                container = org.chorus_oss.protocol.types.inventory.FullContainerName(
                    org.chorus_oss.protocol.types.itemstack.ContainerSlotType.Armor,
                    id
                ),
                storageItem = ItemStack.from(Item.AIR)
            )
            player.sendPacket(packet)
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
