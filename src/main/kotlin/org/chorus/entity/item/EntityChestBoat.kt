package org.chorus.entity.item

import org.chorus.Player
import org.chorus.entity.EntityID
import org.chorus.entity.data.EntityDataTypes
import org.chorus.inventory.*
import org.chorus.item.*
import org.chorus.level.format.IChunk
import org.chorus.math.*
import org.chorus.nbt.NBTIO
import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.ListTag

class EntityChestBoat(chunk: IChunk?, nbt: CompoundTag?) : EntityBoat(chunk, nbt), InventoryHolder {
    override fun getIdentifier(): String {
        return EntityID.CHEST_BOAT
    }

    protected var inventory: Inventory = null

    override fun getOriginalName(): String {
        return "Chest Boat"
    }


    override fun getInventory(): ChestBoatInventory {
        return inventory!!
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        if (player.isSneaking()) {
            player.addWindow(inventory!!)
            return false
        }

        if (passengers.size >= 1 || getWaterLevel() < -SINKING_DEPTH) {
            return false
        }

        super.mountEntity(player)
        return false
    }

    override fun getInteractButtonText(player: Player): String {
        if (player.isSneaking()) {
            return "action.interact.opencontainer"
        }
        return "action.interact.ride.boat"
    }

    public override fun initEntity() {
        super.initEntity()

        this.inventory = ChestBoatInventory(this)
        if (namedTag!!.contains("Items") && namedTag!!.get("Items") is ListTag<*>) {
            val inventoryList: ListTag<CompoundTag> = namedTag!!.getList(
                "Items",
                CompoundTag::class.java
            )
            for (item: CompoundTag in inventoryList.getAll()) {
                inventory!!.setItem(item.getByte("Slot").toInt(), NBTIO.getItemHelper(item))
            }
        }

        entityDataMap.put(EntityDataTypes.Companion.CONTAINER_TYPE, InventoryType.CHEST_BOAT.getNetworkType())
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

    override fun dropItem() {
        when (this.getVariant()) {
            0 -> level!!.dropItem(this.position, Item.get(ItemID.OAK_CHEST_BOAT))
            1 -> level!!.dropItem(this.position, Item.get(ItemID.SPRUCE_CHEST_BOAT))
            2 -> level!!.dropItem(this.position, Item.get(ItemID.BIRCH_CHEST_BOAT))
            3 -> level!!.dropItem(this.position, Item.get(ItemID.JUNGLE_CHEST_BOAT))
            4 -> level!!.dropItem(this.position, Item.get(ItemID.ACACIA_CHEST_BOAT))
            5 -> level!!.dropItem(this.position, Item.get(ItemID.DARK_OAK_CHEST_BOAT))
            6 -> level!!.dropItem(this.position, Item.get(ItemID.MANGROVE_CHEST_BOAT))
            else -> level!!.dropItem(this.position, Item.get(ItemID.CHEST_BOAT))
        }

        for (item: Item? in inventory!!.getContents().values) {
            level!!.dropItem(this.position, item)
        }
        inventory!!.clearAll()
    }
}
