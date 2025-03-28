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


class HumanEnderChestInventory(human: IHuman) : BaseInventory(human, InventoryType.CONTAINER, 27),
    BlockEntityInventoryNameable {
    private var enderChest: BlockEntityEnderChest? = null

    override fun init() {
        val map = super.slotTypeMap()
        for (i in 0..<size) {
            map[i] = ContainerSlotType.LEVEL_ENTITY
        }
    }

    override var holder: InventoryHolder
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
        super.onOpen(who)
        who.dataPacket(ContainerOpenPacket(
            containerID = who.getWindowId(this),
            containerType = type.networkType,
            position = holder.vector3.asBlockVector3(),
            targetActorID = who.getId()
        ))
        this.sendContents(who)

        val blockEventPacket = BlockEventPacket()
        blockEventPacket.x = enderChest!!.x.toInt()
        blockEventPacket.y = enderChest!!.y.toInt()
        blockEventPacket.z = enderChest!!.z.toInt()
        blockEventPacket.type = 1
        blockEventPacket.value = 2

        val level = who.level
        if (level != null) {
            level.addSound(who.vector3.add(0.5, 0.5, 0.5), Sound.RANDOM_ENDERCHESTOPEN)
            level.addChunkPacket(
                who.position.chunkX,
                who.position.chunkZ,
                blockEventPacket
            )
        }
    }

    override fun slotTypeMap(): MutableMap<Int?, ContainerSlotType?> {
        val map = super.slotTypeMap()
        map[SpecialWindowId.CONTAINER_ID_REGISTRY.id] = ContainerSlotType.ANVIL_INPUT
        return map
    }

    override fun onClose(who: Player) {
        if (who !== this.holder) {
            return
        }
        if (enderChest == null) {
            return
        }

        val containerId = who.getWindowId(this)
        who.dataPacket(ContainerClosePacket(
            containerID = containerId,
            containerType = type,
            serverInitiatedClose = who.closingWindowId != containerId
        ))

        val blockEventPacket = BlockEventPacket()
        blockEventPacket.x = enderChest!!.x.toInt()
        blockEventPacket.y = enderChest!!.y.toInt()
        blockEventPacket.z = enderChest!!.z.toInt()
        blockEventPacket.type = 1
        blockEventPacket.value = 0

        val level = who.level
        if (level != null) {
            level.addSound(who.vector3.add(0.5, 0.5, 0.5), Sound.RANDOM_ENDERCHESTCLOSED)
            level.addChunkPacket(
                who.position.chunkX,
                who.position.chunkZ,
                blockEventPacket
            )
        }
        setBlockEntityEnderChest(who, null)
        super.onClose(who)
    }

    override val blockEntityInventoryHolder: BlockEntityNameable?
        get() = enderChest

    override var inventoryTitle: String
        get() {
            return enderChest?.name ?: "Unknown"
        }
        set(name) {
            if (enderChest != null) {
                enderChest!!.name = name
            }
        }
}
