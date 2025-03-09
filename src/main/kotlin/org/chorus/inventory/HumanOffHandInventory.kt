package org.chorus.inventory

import cn.nukkit.Player
import cn.nukkit.entity.Entity.getId
import cn.nukkit.entity.IHuman
import cn.nukkit.entity.IHuman.getEntity
import cn.nukkit.item.*
import cn.nukkit.network.protocol.InventoryContentPacket
import cn.nukkit.network.protocol.MobEquipmentPacket
import cn.nukkit.network.protocol.types.inventory.FullContainerName
import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType

class HumanOffHandInventory(holder: IHuman?) : BaseInventory(holder, InventoryType.INVENTORY, 1) {
    override fun init() {
        val map = super.networkSlotMap()
        map!![0] = 1

        val map2 = super.slotTypeMap()
        map2!![0] = ContainerSlotType.OFFHAND
    }

    fun setItem(item: Item) {
        setItem(0, item)
    }

    fun setItem(item: Item, send: Boolean) {
        setItem(0, item, send)
    }

    override fun setItem(index: Int, item: Item): Boolean {
        return super.setItem(0, item)
    }

    override fun setItem(index: Int, item: Item, send: Boolean): Boolean {
        return super.setItem(0, item, send)
    }

    override fun onSlotChange(index: Int, before: Item, send: Boolean) {
        val holder: IHuman? = this.holder
        if (holder is Player) {
            if (!holder.spawned) return
            if (send) {
                this.sendContents(this.getViewers())
                this.sendContents(holder.getEntity().getViewers().values)
            }
        }
    }

    override fun sendContents(vararg players: Player) {
        val item = this.getItem(0)
        val pk = this.createMobEquipmentPacket(item)

        for (player in players) {
            if (player === this.holder) {
                val pk2 = InventoryContentPacket()
                pk2.inventoryId = SpecialWindowId.OFFHAND.id
                pk2.slots = arrayOf(item)
                pk2.fullContainerName = FullContainerName(
                    ContainerSlotType.OFFHAND,
                    0
                )
                player.dataPacket(pk2)
                player.dataPacket(pk)
            } else {
                player.dataPacket(pk)
            }
        }
    }

    override fun sendSlot(index: Int, vararg players: Player) {
        sendContents(*players)
    }

    private fun createMobEquipmentPacket(item: Item): MobEquipmentPacket {
        val pk = MobEquipmentPacket()
        pk.eid = holder.getEntity().getId()
        pk.item = item
        pk.slot = 1
        pk.containerId = SpecialWindowId.OFFHAND.id
        return pk
    }

    override var holder: InventoryHolder?
        get() = super.getHolder() as IHuman
        set(holder) {
            super.holder = holder
        }
}
