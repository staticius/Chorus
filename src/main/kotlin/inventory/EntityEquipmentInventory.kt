package org.chorus_oss.chorus.inventory

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.entity.Entity
import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.network.protocol.MobEquipmentPacket

class EntityEquipmentInventory(holder: InventoryHolder) :
    BaseInventory(holder, InventoryType.INVENTORY, 9 + 27) {
    val entity: Entity = holder as Entity

    override val size: Int
        get() = 2


    override fun sendSlot(index: Int, vararg players: Player) {
        for (player in players) {
            this.sendSlot(index, player)
        }
    }

    override fun sendSlot(index: Int, player: Player) {
        val mobEquipmentPacket = MobEquipmentPacket()
        mobEquipmentPacket.eid = entity.getRuntimeID()
        mobEquipmentPacket.selectedSlot = index
        mobEquipmentPacket.slot =
            mobEquipmentPacket.selectedSlot //todo check inventorySlot and hotbarSlot for MobEquipmentPacket
        mobEquipmentPacket.item = this.getItem(index)
        player.dataPacket(mobEquipmentPacket)
    }

    override val viewers: MutableSet<Player> = super.viewers
        get() {
            val viewers: MutableSet<Player> = HashSet(field)
            viewers.addAll(entity.viewers.values)
            return viewers
        }

    override fun open(who: Player): Boolean {
        return viewers.add(who)
    }


    val itemInHand: Item
        get() = this.getItem(MAIN_HAND)


    val itemInOffhand: Item
        get() = this.getItem(OFFHAND)


    fun setItemInHand(item: Item?): Boolean {
        return this.setItem(MAIN_HAND, item!!)
    }


    fun setItemInHand(item: Item?, send: Boolean): Boolean {
        return this.setItem(MAIN_HAND, item!!, send)
    }


    fun setItemInOffhand(item: Item?, send: Boolean): Boolean {
        return this.setItem(OFFHAND, item!!, send)
    }

    override fun sendContents(target: Player) {
        this.sendSlot(MAIN_HAND, target)
        this.sendSlot(OFFHAND, target)
    }

    override fun sendContents(vararg target: Player) {
        for (player in target) {
            this.sendContents(player)
        }
    }

    companion object {
        const val MAIN_HAND: Int = 0
        const val OFFHAND: Int = 1
    }
}
