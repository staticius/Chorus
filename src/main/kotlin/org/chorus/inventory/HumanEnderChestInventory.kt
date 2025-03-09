package org.chorus.inventory

import org.chorus.Player
import org.chorus.blockentity.BlockEntityEnderChest
import org.chorus.blockentity.BlockEntityNameable
import org.chorus.entity.IHuman
import org.chorus.level.Sound
import org.chorus.network.protocol.BlockEventPacket
import org.chorus.network.protocol.ContainerClosePacket
import org.chorus.network.protocol.ContainerOpenPacket
import org.chorus.network.protocol.types.itemstack.ContainerSlotType
import lombok.extern.slf4j.Slf4j

@Slf4j
class HumanEnderChestInventory(human: IHuman?) : BaseInventory(human, InventoryType.CONTAINER, 27),
    BlockEntityInventoryNameable {
    private var enderChest: BlockEntityEnderChest? = null

    override fun init() {
        val map = super.slotTypeMap()
        for (i in 0..<getSize()) {
            map!![i] = ContainerSlotType.LEVEL_ENTITY
        }
    }

    override var holder: InventoryHolder?
        get() = holder as IHuman
        set(holder) {
            super.holder = holder
        }

    fun setBlockEntityEnderChest(player: Player, blockEntityEnderChest: BlockEntityEnderChest?) {
        if (blockEntityEnderChest == null) {
            enderChest = null
            player.enderChestOpen = false
        } else {
            enderChest = blockEntityEnderChest
            player.enderChestOpen = true
        }
    }

    override fun onOpen(who: Player) {
        if (who !== this.holder) {
            return
        }
        if (enderChest == null) {
            return
        }
        val containerOpenPacket = ContainerOpenPacket()
        containerOpenPacket.windowId = who.getWindowId(this)
        containerOpenPacket.type = getType().networkType
        containerOpenPacket.x = enderChest!!.x.toInt()
        containerOpenPacket.y = enderChest!!.y.toInt()
        containerOpenPacket.z = enderChest!!.z.toInt()
        super.onOpen(who)
        who.dataPacket(containerOpenPacket)
        this.sendContents(who)

        val blockEventPacket = BlockEventPacket()
        blockEventPacket.x = enderChest!!.x.toInt()
        blockEventPacket.y = enderChest!!.y.toInt()
        blockEventPacket.z = enderChest!!.z.toInt()
        blockEventPacket.type = 1
        blockEventPacket.value = 2

        val level = holder.level
        if (level != null) {
            level.addSound(holder.vector3.add(0.5, 0.5, 0.5), Sound.RANDOM_ENDERCHESTOPEN)
            level.addChunkPacket(
                holder.x.toInt() shr 4,
                holder.z.toInt() shr 4, blockEventPacket
            )
        }
    }

    override fun slotTypeMap(): MutableMap<Int?, ContainerSlotType?> {
        val map = super.slotTypeMap()
        map!![SpecialWindowId.CONTAINER_ID_REGISTRY.id] = ContainerSlotType.ANVIL_INPUT
        return map
    }

    override fun onClose(who: Player) {
        if (who !== this.holder) {
            return
        }
        if (enderChest == null) {
            return
        }

        val containerClosePacket = ContainerClosePacket()
        containerClosePacket.windowId = who.getWindowId(this)
        containerClosePacket.wasServerInitiated = who.closingWindowId != containerClosePacket.windowId
        containerClosePacket.type = getType()
        who.dataPacket(containerClosePacket)

        val blockEventPacket = BlockEventPacket()
        blockEventPacket.x = enderChest!!.x.toInt()
        blockEventPacket.y = enderChest!!.y.toInt()
        blockEventPacket.z = enderChest!!.z.toInt()
        blockEventPacket.type = 1
        blockEventPacket.value = 0

        val level = holder.level
        if (level != null) {
            level.addSound(holder.vector3.add(0.5, 0.5, 0.5), Sound.RANDOM_ENDERCHESTCLOSED)
            level.addChunkPacket(
                holder.x.toInt() shr 4,
                holder.z.toInt() shr 4, blockEventPacket
            )
        }
        setBlockEntityEnderChest(who, null)
        super.onClose(who)
    }

    override val blockEntityInventoryHolder: BlockEntityNameable?
        get() = enderChest

    override var inventoryTitle: String
        get() {
            return if (enderChest != null) {
                enderChest!!.name
            } else "Unknown"
        }
        set(name) {
            if (enderChest != null) {
                enderChest!!.name = name
            }
        }
}
