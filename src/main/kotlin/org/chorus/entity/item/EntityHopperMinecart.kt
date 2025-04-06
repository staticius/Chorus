package org.chorus.entity.item

import org.chorus.Player
import org.chorus.block.*
import org.chorus.block.BlockHopper.IHopper
import org.chorus.blockentity.*
import org.chorus.entity.*
import org.chorus.entity.data.EntityDataTypes
import org.chorus.event.block.HopperSearchItemEvent
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

class EntityHopperMinecart(chunk: IChunk?, nbt: CompoundTag) : EntityMinecartAbstract(chunk, nbt), InventoryHolder,
    IHopper {
    override fun getIdentifier(): String {
        return EntityID.HOPPER_MINECART
    }

    private val temporalVector: BlockVector3 = BlockVector3()
    var transferCooldown: Int = 0

    protected var inventory: Inventory = null
    private var disabled: Boolean = false
    private var pickupArea: AxisAlignedBB? = null

    init {
        setDisplayBlock(Block.get(Block.HOPPER), false)
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (!super.onUpdate(currentTick)) return false

        if (isOnTransferCooldown()) {
            transferCooldown--
            return true
        }

        checkDisabled()

        if (isDisabled()) {
            return false
        }

        val event: HopperSearchItemEvent = HopperSearchItemEvent(this, true)
        Server.instance.pluginManager.callEvent(event)
        if (event.isCancelled) return false

        this.updatePickupArea()

        val blockSide: Block = getLocator().getSide(BlockFace.UP).getTickCachedLevelBlock()
        val blockEntity: BlockEntity =
            level!!.getBlockEntity(temporalVector.setComponentsAdding(this.position, BlockFace.UP))

        val changed: Boolean

        if (blockEntity is InventoryHolder || blockSide is BlockComposter) {
            //从容器中拉取物品
            changed = pullItems(this, this.getLocator())
        } else {
            //收集掉落物
            changed = pickupItems(this, this.getLocator(), pickupArea)
        }

        if (changed) {
            this.setTransferCooldown(1)
        }

        return true
    }

    fun isOnTransferCooldown(): Boolean {
        return this.transferCooldown > 0
    }

    fun setTransferCooldown(transferCooldown: Int) {
        this.transferCooldown = transferCooldown
    }

    override fun getOriginalName(): String {
        return getType().getName()
    }

    override fun getType(): MinecartType {
        return MinecartType.valueOf(5)
    }

    override fun isRideable(): Boolean {
        return false
    }

    override fun dropItem() {
        for (item: Item? in inventory.getContents().values) {
            level!!.dropItem(this.position, item)
        }
        if (lastDamageCause is EntityDamageByEntityEvent) {
            val damager: Entity = lastDamageCause.damager
            if (damager is Player && damager.isCreative()) {
                return
            }
        }
        level!!.dropItem(this.position, Item.get(Item.HOPPER_MINECART))
    }

    override fun kill() {
        super.kill()
        inventory.clearAll()
    }

    override fun mountEntity(entity: Entity, mode: EntityLink.Type): Boolean {
        return false
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        player.addWindow(inventory)
        return false // If true, the count of items player has in hand decreases
    }

    override fun getInventory(): MinecartHopperInventory {
        return inventory
    }

    override fun initEntity() {
        this.inventory = MinecartHopperInventory(this)
        if (namedTag!!.contains("Items") && namedTag!!.get("Items") is ListTag<*>) {
            val inventoryList: ListTag<CompoundTag> = namedTag!!.getList(
                "Items",
                CompoundTag::class.java
            )
            for (item: CompoundTag in inventoryList.getAll()) {
                inventory.setItem(item.getByte("Slot").toInt(), NBTIO.getItemHelper(item))
            }
        }

        entityDataMap.put(EntityDataTypes.Companion.CONTAINER_TYPE, 11)
        entityDataMap.put(EntityDataTypes.Companion.CONTAINER_SIZE, inventory.size)
        entityDataMap.put(EntityDataTypes.Companion.CONTAINER_STRENGTH_MODIFIER, 0)

        this.updatePickupArea()

        this.scheduleUpdate()

        super.initEntity()

        checkDisabled()
    }

    fun updatePickupArea() {
        this.pickupArea = SimpleAxisAlignedBB(
            position.x - 0.5,
            position.y - 0.5,
            position.z - 0.5, position.x + 1, position.y + 2.5, position.z + 1
        ).expand(0.25, 0.0, 0.25)
    }

    fun checkDisabled() {
        if (getLocator().getLevelBlock() is BlockActivatorRail) {
            setDisabled(rail.isActive())
        }
    }

    fun isDisabled(): Boolean {
        return disabled
    }

    fun setDisabled(disabled: Boolean) {
        this.disabled = disabled
    }

    override fun saveNBT() {
        super.saveNBT()
        namedTag!!.putList("Items", ListTag<CompoundTag>())
        if (this.inventory != null) {
            for (slot in 0..4) {
                val item: Item = inventory.getItem(slot)
                if (item != null && !item.isNull()) {
                    namedTag!!.getList("Items", CompoundTag::class.java)
                        .add(NBTIO.putItemHelper(item, slot))
                }
            }
        }
    }
}
