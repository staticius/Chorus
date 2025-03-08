package cn.nukkit.inventory

import cn.nukkit.Player
import cn.nukkit.block.BlockTrappedChest
import cn.nukkit.blockentity.BlockEntity.block
import cn.nukkit.blockentity.BlockEntityChest
import cn.nukkit.blockentity.BlockEntityNameable
import cn.nukkit.event.redstone.RedstoneUpdateEvent
import cn.nukkit.level.Sound
import cn.nukkit.network.protocol.BlockEventPacket
import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType
import cn.nukkit.utils.LevelException
import cn.nukkit.utils.RedstoneComponent

/**
 * @author MagicDroidX (Nukkit Project)
 */
class ChestInventory(chest: BlockEntityChest?) : ContainerInventory(chest, InventoryType.CONTAINER, 27),
    BlockEntityInventoryNameable {
    protected var doubleInventory: DoubleChestInventory? = null

    override fun init() {
        val map = super.slotTypeMap()
        for (i in 0..<getSize()) {
            map!![i] = ContainerSlotType.LEVEL_ENTITY
        }
    }

    override fun slotTypeMap(): MutableMap<Int?, ContainerSlotType?> {
        val map = super.slotTypeMap()
        for (i in 0..<this.getSize()) {
            map!![i] = ContainerSlotType.INVENTORY
        }
        return map!!
    }


    override var holder: InventoryHolder?
        get() = holder as BlockEntityChest
        set(holder) {
            super.holder = holder
        }

    override fun onOpen(who: Player) {
        super.onOpen(who)

        if (getViewers().size == 1) {
            val pk = BlockEventPacket()
            pk.x = holder.x.toInt()
            pk.y = holder.y.toInt()
            pk.z = holder.z.toInt()
            pk.type = 1
            pk.value = 2

            val level = holder.level
            if (level != null) {
                level.addSound(holder.position.add(0.5, 0.5, 0.5), Sound.RANDOM_CHESTOPEN)
                level.addChunkPacket(
                    holder.x.toInt() shr 4,
                    holder.z.toInt() shr 4, pk
                )
            }
        }
        try {
            if (holder.block is BlockTrappedChest) {
                val event = RedstoneUpdateEvent(trappedChest)
                holder!!.level.server.pluginManager.callEvent(event)
                if (!event.isCancelled) {
                    RedstoneComponent.updateAllAroundRedstone(this.holder)
                }
            }
        } catch (ignored: LevelException) {
        }
    }

    override fun onClose(who: Player) {
        if (getViewers().size == 1) {
            val pk = BlockEventPacket()
            pk.x = holder.x.toInt()
            pk.y = holder.y.toInt()
            pk.z = holder.z.toInt()
            pk.type = 1
            pk.value = 0

            val level = holder.level
            if (level != null) {
                level.addSound(holder.position.add(0.5, 0.5, 0.5), Sound.RANDOM_CHESTCLOSED)
                level.addChunkPacket(
                    holder.x.toInt() shr 4,
                    holder.z.toInt() shr 4, pk
                )
            }
        }

        try {
            if (holder.block is BlockTrappedChest) {
                val event = RedstoneUpdateEvent(trappedChest)
                holder!!.level.server.pluginManager.callEvent(event)
                if (!event.isCancelled) {
                    RedstoneComponent.updateAllAroundRedstone(this.holder)
                }
            }
        } catch (ignored: LevelException) {
        }
        super.onClose(who)
    }

    fun setDoubleInventory(doubleInventory: DoubleChestInventory) {
        this.doubleInventory = doubleInventory
    }

    fun getDoubleInventory(): DoubleChestInventory? {
        return doubleInventory
    }

    override fun sendSlot(index: Int, vararg players: Player) {
        if (this.doubleInventory != null) {
            doubleInventory!!.sendSlot(this, index, *players)
        } else {
            super.sendSlot(index, *players)
        }
    }

    override fun canCauseVibration(): Boolean {
        return true
    }

    override val blockEntityInventoryHolder: BlockEntityNameable?
        get() = holder
}
