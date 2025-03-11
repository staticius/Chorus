package org.chorus.inventory

import org.chorus.Player
import org.chorus.Server
import org.chorus.blockentity.*
import org.chorus.entity.Entity
import org.chorus.event.entity.EntityInventoryChangeEvent
import org.chorus.event.inventory.InventoryCloseEvent
import org.chorus.event.inventory.InventoryOpenEvent
import org.chorus.item.*
import org.chorus.network.protocol.InventoryContentPacket
import org.chorus.network.protocol.InventorySlotPacket
import org.chorus.network.protocol.types.inventory.FullContainerName
import org.chorus.network.protocol.types.itemstack.ContainerSlotType
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList

import org.jetbrains.annotations.ApiStatus
import java.util.*
import java.util.function.Consumer
import kotlin.math.max
import kotlin.math.min



abstract class BaseInventory(
    override var holder: InventoryHolder?,
    override val type: InventoryType,
    override val size: Int
) : Inventory {
    protected val slots: HashMap<Int, Item> = HashMap()
    override val viewers: MutableSet<Player?> = HashSet()
    override var maxStackSize: Int = Inventory.Companion.MAX_STACK
    protected var listeners: MutableList<InventoryListener>? = null
    protected var slotTypeMap: MutableMap<Int?, ContainerSlotType?> =
        HashMap()
    protected var networkSlotMap: BiMap<Int, Int> =
        HashBiMap.create()

    init {
        init()
    }

    override fun slotTypeMap(): MutableMap<Int?, ContainerSlotType?> {
        return this.slotTypeMap
    }

    override fun networkSlotMap(): BiMap<Int, Int> {
        return this.networkSlotMap
    }

    override fun getItem(index: Int): Item {
        return if (slots.containsKey(index)) slots[index]!!.clone() else Item.AIR
    }

    override fun getUnclonedItem(index: Int): Item? {
        return slots.getOrDefault(index, Item.AIR)
    }

    override var contents: Map<Int, Item>
        get() = HashMap(this.slots)
        set(items) {
            var items: Map<Int, Item?> = items
            if (items.size > this.size) {
                var newItems =
                    TreeMap(items)
                items = newItems
                newItems = TreeMap()
                var i = 0
                for ((key, value) in items) {
                    newItems[key] = value
                    i++
                    if (i >= this.size) {
                        break
                    }
                }
                items = newItems
            }

            for (i in 0..<this.size) {
                if (!items.containsKey(i)) {
                    if (slots.containsKey(i)) {
                        this.clear(i)
                    }
                } else {
                    if (!this.setItem(i, items[i]!!)) {
                        this.clear(i)
                    }
                }
            }
        }

    @ApiStatus.Internal
    fun setItemInternal(index: Int, item: Item) {
        slots[index] = item
    }

    override fun setItem(index: Int, item: Item, send: Boolean): Boolean {
        var item = item
        if (index < 0 || index >= this.size) {
            return false
        } else if (item.isNull) {
            return this.clear(index, send)
        }

        item = item.clone()
        val holder = this.holder
        if (holder is Entity) {
            val ev = EntityInventoryChangeEvent(holder, this.getItem(index), item, index)
            Server.instance.pluginManager.callEvent(ev)
            if (ev.isCancelled) {
                this.sendSlot(index, this.getViewers())
                return false
            }

            item = ev.newItem
        }

        if (holder is BlockEntity) {
            holder.setDirty()
        }

        val old = this.getUnclonedItem(index)
        slots[index] = item
        this.onSlotChange(index, old!!, send)

        return true
    }

    override fun contains(item: Item): Boolean {
        var count = max(1.0, item.getCount().toDouble()).toInt()
        val checkDamage = item.hasMeta() && item.damage >= 0
        val checkTag = item.compoundTag != null
        for (i in contents.values) {
            if (item.equals(i, checkDamage, checkTag)) {
                count -= i.getCount()
                if (count <= 0) {
                    return true
                }
            }
        }

        return false
    }

    override fun all(item: Item): Map<Int, Item> {
        val slots: MutableMap<Int, Item> = HashMap()
        val checkDamage = item.hasMeta() && item.damage >= 0
        val checkTag = item.compoundTag != null
        for ((key, value) in this.contents) {
            if (item.equals(value, checkDamage, checkTag)) {
                slots[key] = value
            }
        }

        return slots
    }

    override fun remove(item: Item) {
        val checkDamage = item.hasMeta()
        val checkTag = item.compoundTag != null
        for ((key, value) in this.contents) {
            if (item.equals(value, checkDamage, checkTag)) {
                this.clear(key)
            }
        }
    }

    override fun first(item: Item, exact: Boolean): Int {
        val count = max(1.0, item.getCount().toDouble()).toInt()
        val checkDamage = item.hasMeta()
        val checkTag = item.compoundTag != null
        for ((key, value) in this.contents) {
            if (item.equals(
                    value,
                    checkDamage,
                    checkTag
                ) && (value.getCount() == count || (!exact && value.getCount() > count))
            ) {
                return key
            }
        }

        return -1
    }

    override fun firstEmpty(item: Item?): Int {
        for (i in 0..<this.size) {
            if (getUnclonedItem(i)!!.isNull) {
                return i
            }
        }

        return -1
    }

    override fun decreaseCount(slot: Int) {
        var item = this.getUnclonedItem(slot)

        if (item!!.getCount() > 0) {
            item = item.clone()
            item.count--
            this.setItem(slot, item)
        }
    }

    override fun canAddItem(item: Item): Boolean {
        var item = item
        item = item.clone()
        val checkDamage = item.hasMeta()
        val checkTag = item.compoundTag != null
        for (i in 0..<this.size) {
            val slot = this.getUnclonedItem(i)
            if (item.equals(slot, checkDamage, checkTag)) {
                val diff: Int
                if (((min(slot!!.maxStackSize.toDouble(), maxStackSize.toDouble()) - slot.getCount()).also {
                        diff =
                            it.toInt()
                    }) > 0) {
                    item.setCount(item.getCount() - diff)
                }
            } else if (slot!!.isNull) {
                item.setCount(
                    (item.getCount() - min(
                        slot.maxStackSize.toDouble(),
                        maxStackSize.toDouble()
                    )).toInt()
                )
            }

            if (item.getCount() <= 0) {
                return true
            }
        }

        return false
    }

    override fun addItem(vararg slots: Item): Array<Item?> {
        val itemSlots: MutableList<Item> = ArrayList()
        for (slot in slots) {
            if (!slot.isNull) {
                //todo: clone only if necessary
                itemSlots.add(slot.clone())
            }
        }

        //使用FastUtils的IntArrayList提高性能
        val emptySlots: IntList = IntArrayList(this.size)

        for (i in 0..<this.size) {
            //获取未克隆Item对象
            var item = this.getUnclonedItem(i)
            if (item!!.isNull || item.getCount() <= 0) {
                emptySlots.add(i)
            }

            //使用迭代器而不是新建一个ArrayList
            val iterator = itemSlots.iterator()
            while (iterator.hasNext()) {
                val slot = iterator.next()
                if (slot == item) {
                    val maxStackSize = min(maxStackSize.toDouble(), item.maxStackSize.toDouble()).toInt()
                    if (item.getCount() < maxStackSize) {
                        var amount =
                            min((maxStackSize - item.getCount()).toDouble(), slot.getCount().toDouble()).toInt()
                        amount = min(amount.toDouble(), this.maxStackSize.toDouble()).toInt()
                        if (amount > 0) {
                            //在需要clone时再clone
                            item = item.clone()
                            slot.setCount(slot.getCount() - amount)
                            item.setCount(item.getCount() + amount)
                            this.setItem(i, item)
                            if (slot.getCount() <= 0) {
                                iterator.remove()
                            }
                        }
                    }
                }
            }
            if (itemSlots.isEmpty()) {
                break
            }
        }

        if (!itemSlots.isEmpty() && !emptySlots.isEmpty()) {
            for (slotIndex in emptySlots) {
                if (!itemSlots.isEmpty()) {
                    val slot = itemSlots[0]
                    val maxStackSize = min(
                        slot.maxStackSize.toDouble(),
                        maxStackSize.toDouble()
                    ).toInt()
                    var amount = min(maxStackSize.toDouble(), slot.getCount().toDouble()).toInt()
                    amount = min(amount.toDouble(), this.maxStackSize.toDouble()).toInt()
                    slot.setCount(slot.getCount() - amount)
                    val item = slot.clone()
                    item.setCount(amount)
                    this.setItem(slotIndex, item)
                    if (slot.getCount() <= 0) {
                        itemSlots.remove(slot)
                    }
                }
            }
        }

        return itemSlots.toArray(Item.EMPTY_ARRAY)
    }

    override fun removeItem(vararg slots: Item): Array<Item?> {
        val itemSlots: MutableList<Item> = ArrayList()
        for (slot in slots) {
            if (!slot.isNull) {
                itemSlots.add(slot.clone())
            }
        }

        for (i in 0..<this.size) {
            var item = this.getUnclonedItem(i)
            if (item!!.isNull || item.getCount() <= 0) {
                continue
            }

            val iterator = itemSlots.iterator()
            while (iterator.hasNext()) {
                val slot = iterator.next()
                if (slot.equals(item, item!!.hasMeta(), item.compoundTag != null)) {
                    item = item.clone()
                    val amount = min(item.getCount().toDouble(), slot.getCount().toDouble()).toInt()
                    slot.setCount(slot.getCount() - amount)
                    item.setCount(item.getCount() - amount)
                    this.setItem(i, item)
                    if (slot.getCount() <= 0) {
                        iterator.remove()
                    }
                }
            }

            if (itemSlots.isEmpty()) {
                break
            }
        }

        return itemSlots.toArray(Item.EMPTY_ARRAY)
    }

    override fun clear(index: Int, send: Boolean): Boolean {
        if (slots.containsKey(index)) {
            var item = Item.AIR
            val old = slots[index]
            val holder = this.holder
            if (holder is Entity) {
                val ev = EntityInventoryChangeEvent(
                    holder as Entity,
                    old!!, item, index
                )
                Server.instance.pluginManager.callEvent(ev)
                if (ev.isCancelled) {
                    this.sendSlot(index, this.getViewers())
                    return false
                }
                item = ev.newItem
            }

            if (!item.isNull) {
                slots[index] = item.clone()
            } else {
                slots.remove(index)
            }

            this.onSlotChange(index, old!!, send)
        }

        return true
    }

    override fun clearAll() {
        for (index in contents.keys) {
            this.clear(index)
        }
    }

    override fun getViewers(): Set<Player?> {
        return viewers
    }

    override fun open(who: Player): Boolean {
        val ev = InventoryOpenEvent(this, who)
        Server.instance.pluginManager.callEvent(ev)
        if (ev.isCancelled) {
            return false
        }
        this.onOpen(who)

        return true
    }

    override fun close(who: Player) {
        val ev = InventoryCloseEvent(this, who)
        Server.instance.pluginManager.callEvent(ev)
        this.onClose(who)
    }

    override fun onOpen(who: Player) {
        viewers.add(who)
    }

    override fun onClose(who: Player) {
        viewers.remove(who)
    }

    override fun onSlotChange(index: Int, before: Item, send: Boolean) {
        if (send) {
            this.sendSlot(index, this.getViewers())
        }

        if (holder is BlockEntity) {
            holder.setDirty()
        }

        if (before.id == ItemID.LODESTONE_COMPASS || getUnclonedItem(index)!!.id == ItemID.LODESTONE_COMPASS) {
            if (holder is Player) {
                holder.updateTrackingPositions(true)
            }
            getViewers().forEach(Consumer { p: Player? -> p!!.updateTrackingPositions(true) })
        }


        if (this.listeners != null) {
            for (listener in listeners!!) {
                listener.onInventoryChanged(this, before, index)
            }
        }
    }

    override fun sendContents(player: Player) {
        this.sendContents(*arrayOf(player))
    }

    override fun sendContents(vararg players: Player) {
        val pk = InventoryContentPacket()
        pk.slots = arrayOfNulls(this.size)
        for (i in 0..<this.size) {
            pk.slots[i] = this.getUnclonedItem(i)
        }

        for (player in players) {
            val id = player.getWindowId(this)
            if (id == -1 || !player.spawned) {
                this.close(player)
                continue
            }
            pk.inventoryId = id
            player.dataPacket(pk)
        }
    }

    override val isFull: Boolean
        get() {
            if (slots.size < this.size) {
                return false
            }

            for (item in slots.values) {
                if (item == null || item.isNull || item.getCount() < item.maxStackSize || item.getCount() < this.maxStackSize) {
                    return false
                }
            }

            return true
        }

    override val isEmpty: Boolean
        get() {
            if (this.maxStackSize <= 0) {
                return false
            }

            for (item in slots.values) {
                if (item != null && !item.isNull) {
                    return false
                }
            }

            return true
        }

    /**
     * 检测指定物品能在该库存所能存放的空余数量
     *
     * @param item 要检测的物品
     * @return 所能存放的空余数量
     */
    override fun getFreeSpace(item: Item): Int {
        val maxStackSize = min(
            item.maxStackSize.toDouble(),
            maxStackSize.toDouble()
        ).toInt()
        var space = (this.size - slots.size) * maxStackSize

        for (slot in contents.values) {
            if (slot.isNull) {
                space += maxStackSize
                continue
            }
            if (slot.equals(item, true, true)) {
                space += maxStackSize - slot.getCount()
            }
        }
        return space
    }

    override fun sendContents(players: Collection<Player?>) {
        this.sendContents(*players.toTypedArray())
    }

    override fun sendSlot(index: Int, player: Player) {
        this.sendSlot(index, *arrayOf(player))
    }

    override fun sendSlot(index: Int, vararg players: Player) {
        val pk = InventorySlotPacket()
        val slot = toNetworkSlot(index)
        pk.slot = slot
        pk.item = this.getUnclonedItem(index)

        for (player in players) {
            val id = player.getWindowId(this)
            if (id == -1) {
                this.close(player)
                continue
            }
            pk.inventoryId = id
            pk.fullContainerName = FullContainerName(
                this.getSlotType(slot),
                id
            )
            player.dataPacket(pk)
        }
    }

    override fun sendSlot(index: Int, players: Collection<Player?>) {
        this.sendSlot(index, *players.toTypedArray())
    }


    override fun addListener(listener: InventoryListener) {
        if (this.listeners == null) {
            this.listeners = ArrayList()
        }
        listeners!!.add(listener)
    }


    override fun removeListener(listener: InventoryListener) {
        if (this.listeners != null) {
            listeners!!.remove(listener)
        }
    }

    fun isValidSlot(index: Int): Boolean {
        return index >= 0 && index < slots.size
    }
}
