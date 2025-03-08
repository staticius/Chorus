package cn.nukkit.entity.item

import cn.nukkit.Player
import cn.nukkit.entity.EntityID
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.inventory.*
import cn.nukkit.item.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.*
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.network.protocol.AddEntityPacket
import cn.nukkit.network.protocol.DataPacket
import cn.nukkit.network.protocol.types.EntityLink

class EntityChestBoat(chunk: IChunk?, nbt: CompoundTag?) : EntityBoat(chunk, nbt), InventoryHolder {
    override fun getIdentifier(): String {
        return EntityID.Companion.CHEST_BOAT
    }

    protected var inventory: ChestBoatInventory? = null

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

        if (passengers.size >= 1 || getWaterLevel() < -EntityBoat.Companion.SINKING_DEPTH) {
            return false
        }

        super.mountEntity(player)
        return false
    }

    override fun createAddEntityPacket(): DataPacket {
        val addEntity: AddEntityPacket = AddEntityPacket()
        addEntity.type = 0
        addEntity.id = "minecraft:chest_boat"
        addEntity.entityUniqueId = this.getId()
        addEntity.entityRuntimeId = this.getId()
        addEntity.yaw = rotation.yaw.toFloat()
        addEntity.headYaw = rotation.yaw.toFloat()
        addEntity.pitch = rotation.pitch.toFloat()
        addEntity.x = position.south.toFloat()
        addEntity.y = position.up.toFloat() + getBaseOffset()
        addEntity.z = position.west.toFloat()
        addEntity.speedX = motion.south.toFloat()
        addEntity.speedY = motion.up.toFloat()
        addEntity.speedZ = motion.west.toFloat()
        addEntity.entityData = this.entityDataMap

        addEntity.links = arrayOfNulls(passengers.size)
        for (i in addEntity.links.indices) {
            addEntity.links.get(i) = EntityLink(
                this.getId(),
                passengers.get(i)!!.getId(),
                if (i == 0) EntityLink.Type.RIDER else EntityLink.Type.PASSENGER,
                false,
                false
            )
        }

        return addEntity
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
