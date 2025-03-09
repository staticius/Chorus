package org.chorus.entity

import cn.nukkit.Player
import cn.nukkit.inventory.*
import cn.nukkit.item.*
import cn.nukkit.network.protocol.MobArmorEquipmentPacket
import cn.nukkit.network.protocol.MobEquipmentPacket

class EntityEquipment(holder: InventoryHolder) : BaseInventory(holder, InventoryType.INVENTORY, 6) {
    private val entity: Entity

    /**
     * @param holder an Entity which implements [InventoryHolder].
     * @throws ClassCastException if the entity does not implements [InventoryHolder]
     */
    init {
        this.entity = holder as Entity
    }

    override fun getSize(): Int {
        return 6
    }

    fun getEntity(): Entity {
        return entity
    }

    override fun getViewers(): Set<Player> {
        val viewers: MutableSet<Player> = HashSet(this.viewers)
        viewers.addAll(entity.getViewers().values())
        return viewers
    }

    override fun open(who: Player): Boolean {
        return viewers.add(who)
    }

    fun getMainHand(): Item {
        return this.getItem(MAIN_HAND)
    }

    fun getOffHand(): Item {
        return this.getItem(OFF_HAND)
    }

    fun setMainHand(item: Item?): Boolean {
        return this.setMainHand(item, true)
    }

    fun setMainHand(item: Item?, send: Boolean): Boolean {
        return this.setItem(MAIN_HAND, item, send)
    }

    fun setOffHand(item: Item?): Boolean {
        return this.setOffHand(item, true)
    }

    fun setOffHand(item: Item?, send: Boolean): Boolean {
        return this.setItem(OFF_HAND, item, send)
    }

    fun getArmor(): List<Item> {
        val armor: List<Item> = ArrayList()
        armor.add(HEAD, this.getHead())
        armor.add(CHEST, this.getChest())
        armor.add(LEGS, this.getLegs())
        armor.add(FEET, this.getFeet())
        return armor
    }

    fun getHead(): Item {
        return this.getItem(HEAD)
    }

    fun getChest(): Item {
        return this.getItem(CHEST)
    }

    fun getLegs(): Item {
        return this.getItem(LEGS)
    }

    fun getFeet(): Item {
        return this.getItem(FEET)
    }

    fun setArmor(items: List<Item>): Boolean {
        return this.setArmor(items, true)
    }

    fun setArmor(items: List<Item>, send: Boolean): Boolean {
        val head: Boolean = this.setHead(items.get(HEAD), send)
        val chest: Boolean = this.setChest(items.get(CHEST), send)
        val legs: Boolean = this.setLegs(items.get(LEGS), send)
        val feet: Boolean = this.setFeet(items.get(FEET), send)
        return head && chest && legs && feet
    }

    fun setHead(item: Item?): Boolean {
        return this.setHead(item, true)
    }

    fun setHead(item: Item?, send: Boolean): Boolean {
        return this.setItem(HEAD, item, send)
    }

    fun setChest(item: Item?): Boolean {
        return this.setChest(item, true)
    }

    fun setChest(item: Item?, send: Boolean): Boolean {
        return this.setItem(CHEST, item, send)
    }

    fun setLegs(item: Item?): Boolean {
        return this.setLegs(item, true)
    }

    fun setLegs(item: Item?, send: Boolean): Boolean {
        return this.setItem(LEGS, item, send)
    }

    fun setFeet(item: Item?): Boolean {
        return this.setFeet(item, true)
    }

    fun setFeet(item: Item?, send: Boolean): Boolean {
        return this.setItem(FEET, item, send)
    }

    fun canEquipByDispenser(): Boolean {
        return true
    }

    fun equip(item: Item): Boolean {
        if (item.isHelmet()) {
            if (item.getTier() > getHead().getTier()) {
                entity.level!!.dropItem(entity.position, getHead())
                this.setHead(item)
                return true
            }
        } else if (item.isChestplate()) {
            if (item.getTier() > getChest().getTier()) {
                entity.level!!.dropItem(entity.position, getChest())
                this.setChest(item)
                return true
            }
        } else if (item.isLeggings()) {
            if (item.getTier() > getLegs().getTier()) {
                entity.level!!.dropItem(entity.position, getLegs())
                this.setLegs(item)
                return true
            }
        } else if (item.isBoots()) {
            if (item.getTier() > getFeet().getTier()) {
                entity.level!!.dropItem(entity.position, getFeet())
                this.setFeet(item)
                return true
            }
        } else if (item.getTier() > getMainHand().getTier()) {
            entity.level!!.dropItem(entity.position, getMainHand())
            this.setMainHand(item)
            return true
        }
        return false
    }

    override fun sendSlot(index: Int, vararg players: Player) {
        for (player: Player in players) {
            this.sendSlot(index, player)
        }
    }

    override fun sendSlot(index: Int, player: Player) {
        when (index) {
            MAIN_HAND, OFF_HAND -> {
                val packet: MobEquipmentPacket = MobEquipmentPacket()
                packet.eid = entity.getId()
                packet.slot = index - 4
                packet.selectedSlot = 0
                packet.item = this.getItem(index)
                player.dataPacket(packet)
            }

            HEAD, CHEST, LEGS, FEET -> {
                val packet: MobArmorEquipmentPacket = MobArmorEquipmentPacket()
                packet.eid = entity.getId()
                packet.slots = getArmor().toArray(Item.EMPTY_ARRAY)
                player.dataPacket(packet)
            }

            else -> throw IllegalStateException("Unexpected value: " + index)
        }
    }

    override fun sendContents(target: Player) {
        this.sendSlot(HEAD, target)
        this.sendSlot(CHEST, target)
        this.sendSlot(LEGS, target)
        this.sendSlot(FEET, target)
        this.sendSlot(MAIN_HAND, target)
        this.sendSlot(OFF_HAND, target)
    }

    override fun sendContents(vararg target: Player) {
        for (player: Player in target) {
            this.sendContents(player)
        }
    }

    companion object {
        const val HEAD: Int = 0
        const val CHEST: Int = 1
        const val LEGS: Int = 2
        const val FEET: Int = 3

        const val MAIN_HAND: Int = 4
        const val OFF_HAND: Int = 5
    }
}
