package org.chorus.inventory

import org.chorus.Player
import org.chorus.Server
import org.chorus.blockentity.BlockEntityCommandBlock
import org.chorus.blockentity.BlockEntityNameable
import org.chorus.event.inventory.InventoryOpenEvent
import org.chorus.item.Item
import org.chorus.math.Vector3
import org.chorus.network.protocol.ContainerClosePacket
import org.chorus.network.protocol.ContainerOpenPacket

//implement the command block's ui
class CommandBlockInventory(override val holder: BlockEntityCommandBlock) : Inventory,
    BlockEntityInventoryNameable {
    override val viewers: MutableSet<Player> = HashSet()
    private var listeners: MutableList<InventoryListener>? = null

    override val size: Int
        get() = 0

    override var maxStackSize: Int
        get() = 0
        set(size) {
        }

    override fun getItem(index: Int): Item {
        return Item.AIR
    }

    override fun setItem(index: Int, item: Item, send: Boolean): Boolean {
        return false
    }

    override fun addItem(vararg slots: Item): Array<Item?> {
        return arrayOfNulls(0)
    }

    override fun canAddItem(item: Item): Boolean {
        return false
    }

    override fun removeItem(vararg slots: Item): Array<Item?> {
        return arrayOfNulls(0)
    }

    override var contents: Map<Int, Item>
        get() = emptyMap()
        set(items) {
        }

    override fun sendContents(player: Player) {
    }

    override fun sendContents(vararg players: Player) {
    }

    override fun sendContents(players: Collection<Player?>) {
    }

    override fun sendSlot(index: Int, player: Player) {
    }

    override fun sendSlot(index: Int, vararg players: Player) {
    }

    override fun sendSlot(index: Int, players: Collection<Player?>) {
    }

    override fun getFreeSpace(item: Item): Int {
        return 0
    }

    override fun contains(item: Item): Boolean {
        return false
    }

    override fun all(item: Item): Map<Int, Item> {
        return emptyMap()
    }

    override fun first(item: Item, exact: Boolean): Int {
        return 0
    }

    override fun firstEmpty(item: Item?): Int {
        return 0
    }

    override fun decreaseCount(slot: Int) {
    }

    override fun remove(item: Item) {
    }

    override fun clear(index: Int, send: Boolean): Boolean {
        return false
    }

    override fun clearAll() {
    }

    override val isFull: Boolean
        get() = true

    override val isEmpty: Boolean
        get() = true

    override fun getViewers(): Set<Player?> {
        return emptySet<Player>()
    }

    override val type: InventoryType
        get() = InventoryType.COMMAND_BLOCK

    override fun getHolder(): BlockEntityCommandBlock {
        return this.holder
    }

    override fun onOpen(who: Player) {
        if (who.isOp && who.isCreative) {
            viewers.add(who)
            val pk = ContainerOpenPacket()
            pk.windowId = who.getWindowId(this)
            pk.type = type.networkType
            val holder: InventoryHolder? = this.getHolder()
            if (holder is Vector3) {
                pk.x = holder.floorX
                pk.y = holder.floorY
                pk.z = holder.floorZ
            } else {
                pk.z = 0
                pk.y = pk.z
                pk.x = pk.y
            }
            who.dataPacket(pk)
        }
    }

    override fun open(who: Player): Boolean {
        if (who.getWindowId(this) != -1) { //todo hack, ContainerClosePacket no longer triggers for command block and struct block, finding the correct way to close them
            who.removeWindow(this)
        }

        val ev = InventoryOpenEvent(this, who)
        Server.instance.pluginManager.callEvent(ev)
        if (ev.isCancelled) {
            return false
        }
        this.onOpen(who)

        return true
    }

    override fun close(who: Player) {
        this.onClose(who)
    }

    override fun onClose(who: Player) {
        val pk = ContainerClosePacket()
        pk.windowId = who.getWindowId(this)
        pk.wasServerInitiated = who.closingWindowId != pk.windowId
        pk.type = type
        who.dataPacket(pk)
        viewers.remove(who)
    }

    override fun onSlotChange(index: Int, before: Item, send: Boolean) {
        if (this.listeners != null) {
            for (listener in listeners!!) {
                listener.onInventoryChanged(this, before, index)
            }
        }
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

    override val blockEntityInventoryHolder: BlockEntityNameable?
        get() = getHolder()
}
