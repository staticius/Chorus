package org.chorus.inventory

import org.chorus.Player
import org.chorus.blockentity.BlockEntityBeacon
import org.chorus.blockentity.BlockEntityNameable
import org.chorus.network.protocol.ContainerOpenPacket
import org.chorus.network.protocol.types.itemstack.ContainerSlotType

/**
 * @author Rover656
 */
class BeaconInventory(blockBeacon: BlockEntityBeacon) : BaseInventory(blockBeacon, InventoryType.BEACON, 1),
    BlockEntityInventoryNameable {
    override fun init() {
        val map = super.slotTypeMap()
        map[0] = ContainerSlotType.BEACON_PAYMENT

        val networkSlotMap = super.networkSlotMap()
        networkSlotMap[0] = 27
    }

    override fun onClose(who: Player) {
        super.onClose(who)

        val drops = who.inventory.addItem(this.getItem(0))
        for (drop in drops) {
            if (!who.dropItem(drop)) {
                holder.level!!.dropItem(holder.vector3.add(0.5, 0.5, 0.5), drop)
            }
        }

        this.clear(0)
    }

    override fun onOpen(who: Player) {
        super.onOpen(who)
        who.dataPacket(ContainerOpenPacket(
            containerID = who.getWindowId(this),
            containerType = type.networkType,
            position = holder.vector3.asBlockVector3(),
            targetActorID = who.getRuntimeID()
        ))
        this.sendContents(who)
    }

    override var holder: InventoryHolder
        get() = super.holder as BlockEntityBeacon
        set(holder) {
            super.holder = holder
        }

    override val blockEntityInventoryHolder: BlockEntityNameable
        get() = holder as BlockEntityNameable
}
