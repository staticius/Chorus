package org.chorus.entity.item

import org.chorus.Player
import org.chorus.block.*
import org.chorus.entity.*
import org.chorus.entity.data.EntityDataTypes
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.inventory.*
import org.chorus.item.*
import org.chorus.level.format.IChunk
import org.chorus.math.*
import org.chorus.nbt.NBTIO
import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.ListTag
import org.chorus.network.protocol.types.EntityLink
import org.chorus.utils.MinecartType

/**
 * @author Snake1999
 * @since 2016/1/30
 */
class EntityChestMinecart(chunk: IChunk?, nbt: CompoundTag) : EntityMinecartAbstract(chunk, nbt), InventoryHolder {
    override fun getIdentifier(): String {
        return EntityID.CHEST_MINECART
    }

    protected var inventory: Inventory = null

    init {
        setDisplayBlock(Block.get(Block.CHEST), false)
    }

    override fun getOriginalName(): String {
        return getType().getName()
    }

    override fun getType(): MinecartType {
        return MinecartType.valueOf(1)
    }

    override fun isRideable(): Boolean {
        return false
    }


    override fun dropItem() {
        for (item: Item? in inventory!!.getContents().values) {
            level!!.dropItem(this.position, item)
        }

        if (lastDamageCause is EntityDamageByEntityEvent) {
            val damager: Entity = lastDamageCause.damager
            if (damager is Player && damager.isCreative()) {
                return
            }
        }
        level!!.dropItem(this.position, Item.get(Item.CHEST_MINECART))
    }

    override fun kill() {
        super.kill()
        inventory!!.clearAll()
    }

    override fun mountEntity(entity: Entity, mode: EntityLink.Type): Boolean {
        return false
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        player.addWindow(inventory!!)
        return false // If true, the count of items player has in hand decreases
    }

    override fun getInventory(): MinecartChestInventory {
        return inventory!!
    }

    override fun initEntity() {
        super.initEntity()

        this.inventory = MinecartChestInventory(this)
        if (namedTag!!.contains("Items") && namedTag!!.get("Items") is ListTag<*>) {
            val inventoryList: ListTag<CompoundTag> = namedTag!!.getList(
                "Items",
                CompoundTag::class.java
            )
            for (item: CompoundTag in inventoryList.getAll()) {
                inventory!!.setItem(item.getByte("Slot").toInt(), NBTIO.getItemHelper(item))
            }
        }

        entityDataMap.put(EntityDataTypes.Companion.CONTAINER_TYPE, 10)
        entityDataMap.put(EntityDataTypes.Companion.CONTAINER_SIZE, inventory!!.size)
        entityDataMap.put(EntityDataTypes.Companion.CONTAINER_STRENGTH_MODIFIER, 0)
    }

    override fun saveNBT() {
        super.saveNBT()

        namedTag!!.putList("Items", ListTag<CompoundTag>())
        if (this.inventory != null) {
            for (slot in 0..26) {
                val item: Item = inventory!!.getItem(slot)
                if (item != null && !item.isNull()) {
                    namedTag!!.getList("Items", CompoundTag::class.java)
                        .add(NBTIO.putItemHelper(item, slot))
                }
            }
        }
    }
}
