package cn.nukkit.inventory

import cn.nukkit.Player
import cn.nukkit.blockentity.BlockEntityChest
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.network.protocol.BlockEventPacket
import cn.nukkit.network.protocol.InventorySlotPacket
import cn.nukkit.network.protocol.types.inventory.FullContainerName
import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType

/**
 * @author MagicDroidX (Nukkit Project)
 */
class DoubleChestInventory(left: BlockEntityChest, right: BlockEntityChest) :
    ContainerInventory(null, InventoryType.CONTAINER, 27 + 27) {
    val leftSide: ChestInventory = left.realInventory
    val rightSide: ChestInventory

    init {
        leftSide.setDoubleInventory(this)

        this.rightSide = right.realInventory
        rightSide.setDoubleInventory(this)

        val items: MutableMap<Int, Item?> = HashMap()
        // First we add the items from the left chest
        for (idx in 0..<leftSide.getSize()) {
            if (leftSide.contents.containsKey(idx)) { // Don't forget to skip empty slots!
                items[idx] = leftSide.contents[idx]
            }
        }
        // And them the items from the right chest
        for (idx in 0..<rightSide.getSize()) {
            if (rightSide.contents.containsKey(idx)) { // Don't forget to skip empty slots!
                items[idx + leftSide.getSize()] =
                    rightSide.contents[idx] // idx + this.left.getSize() so we don't overlap left chest items
            }
        }

        this.contents = items
    }

    override fun init() {
        val map = super.slotTypeMap()
        for (i in 0..<getSize()) {
            map!![i] = ContainerSlotType.LEVEL_ENTITY
        }
    }

    override var holder: InventoryHolder?
        get() = leftSide.getHolder()
        set(holder) {
            super.holder = holder
        }

    override fun getItem(index: Int): Item {
        return if (index < leftSide.getSize()) leftSide.getItem(index) else rightSide.getItem(index - rightSide.getSize())
    }


    override fun getUnclonedItem(index: Int): Item? {
        return if (index < leftSide.getSize()) leftSide.getUnclonedItem(index) else rightSide.getUnclonedItem(index - rightSide.getSize())
    }

    override fun setItem(index: Int, item: Item, send: Boolean): Boolean {
        return if (index < leftSide.getSize()) leftSide.setItem(
            index,
            item,
            send
        ) else rightSide.setItem(index - rightSide.getSize(), item, send)
    }

    override fun clear(index: Int, send: Boolean): Boolean {
        return if (index < leftSide.getSize()) leftSide.clear(
            index,
            send
        ) else rightSide.clear(index - rightSide.getSize(), send)
    }

    override var contents: Map<Int, Item>
        get() {
            val contents: MutableMap<Int, Item> =
                HashMap()

            for (i in 0..<this.getSize()) {
                contents[i] = getItem(i)
            }

            return contents
        }
        set(items) {
            if (items.size > this.size) {
                items.keys.removeIf { slot: Int -> slot >= this.size }
            }

            for (i in 0..<this.size) {
                val item = items[i]
                var isSet = false

                if (item != null) {
                    isSet = if (i < leftSide.size) {
                        leftSide.setItem(i, item)
                    } else {
                        rightSide.setItem(i - leftSide.size, item)
                    }
                }

                if (!isSet) {
                    this.clear(i)
                }
            }
        }

    override fun onOpen(who: Player) {
        super.onOpen(who)
        leftSide.viewers.add(who)
        rightSide.viewers.add(who)

        if (getViewers().size == 1) {
            val pk1 = BlockEventPacket()
            pk1.x = leftSide.getHolder().x.toInt()
            pk1.y = leftSide.getHolder().y.toInt()
            pk1.z = leftSide.getHolder().z.toInt()
            pk1.type = 1
            pk1.value = 2
            var level = leftSide.getHolder().getLevel()
            if (level != null) {
                level.addSound(leftSide.getHolder().position.add(0.5, 0.5, 0.5), Sound.RANDOM_CHESTOPEN)
                level.addChunkPacket(
                    leftSide.getHolder().x.toInt() shr 4,
                    leftSide.getHolder().z.toInt() shr 4, pk1
                )
            }

            val pk2 = BlockEventPacket()
            pk2.x = rightSide.getHolder().x.toInt()
            pk2.y = rightSide.getHolder().y.toInt()
            pk2.z = rightSide.getHolder().z.toInt()
            pk2.type = 1
            pk2.value = 2

            level = rightSide.getHolder().getLevel()
            if (level != null) {
                level.addSound(rightSide.getHolder().position.add(0.5, 0.5, 0.5), Sound.RANDOM_CHESTOPEN)
                level.addChunkPacket(
                    rightSide.getHolder().x.toInt() shr 4,
                    rightSide.getHolder().z.toInt() shr 4, pk2
                )
            }
        }
    }

    override fun onClose(who: Player) {
        if (getViewers().size == 1) {
            val pk1 = BlockEventPacket()
            pk1.x = rightSide.getHolder().x.toInt()
            pk1.y = rightSide.getHolder().y.toInt()
            pk1.z = rightSide.getHolder().z.toInt()
            pk1.type = 1
            pk1.value = 0

            var level = rightSide.getHolder().getLevel()
            if (level != null) {
                level.addSound(rightSide.getHolder().position.add(0.5, 0.5, 0.5), Sound.RANDOM_CHESTCLOSED)
                level.addChunkPacket(
                    rightSide.getHolder().x.toInt() shr 4,
                    rightSide.getHolder().z.toInt() shr 4, pk1
                )
            }

            val pk2 = BlockEventPacket()
            pk2.x = leftSide.getHolder().x.toInt()
            pk2.y = leftSide.getHolder().y.toInt()
            pk2.z = leftSide.getHolder().z.toInt()
            pk2.type = 1
            pk2.value = 0

            level = leftSide.getHolder().getLevel()
            if (level != null) {
                level.addSound(leftSide.getHolder().position.add(0.5, 0.5, 0.5), Sound.RANDOM_CHESTCLOSED)
                level.addChunkPacket(
                    leftSide.getHolder().x.toInt() shr 4,
                    leftSide.getHolder().z.toInt() shr 4, pk2
                )
            }
        }

        leftSide.viewers.remove(who)
        rightSide.viewers.remove(who)
        super.onClose(who)
    }

    fun sendSlot(inv: Inventory, index: Int, vararg players: Player) {
        val pk = InventorySlotPacket()
        val i = if (inv === this.rightSide) leftSide.getSize() + index else index
        pk.slot = toNetworkSlot(i)
        pk.item = inv.getUnclonedItem(index)

        for (player in players) {
            val id = player.getWindowId(this)
            if (id == -1) {
                this.close(player)
                continue
            }
            pk.inventoryId = id
            pk.fullContainerName = FullContainerName(
                this.getSlotType(pk.slot),
                id
            )
            player.dataPacket(pk)
        }
    }
}
