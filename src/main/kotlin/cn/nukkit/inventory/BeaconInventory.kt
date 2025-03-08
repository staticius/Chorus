package cn.nukkit.inventory

import cn.nukkit.Player
import cn.nukkit.blockentity.BlockEntityBeacon
import cn.nukkit.blockentity.BlockEntityNameable
import cn.nukkit.network.protocol.ContainerOpenPacket
import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType

/**
 * @author Rover656
 */
class BeaconInventory(blockBeacon: BlockEntityBeacon?) : BaseInventory(blockBeacon, InventoryType.BEACON, 1),
    BlockEntityInventoryNameable {
    override fun init() {
        val map = super.slotTypeMap()
        map!![0] = ContainerSlotType.BEACON_PAYMENT

        val networkSlotMap = super.networkSlotMap()
        networkSlotMap!![0] = 27
    }

    override fun onClose(who: Player) {
        super.onClose(who)

        val drops = who.inventory.addItem(this.getItem(0))
        for (drop in drops) {
            if (!who.dropItem(drop)) {
                holder.level.dropItem(holder.vector3.add(0.5, 0.5, 0.5), drop)
            }
        }

        this.clear(0)
    }

    override fun onOpen(who: Player) {
        super.onOpen(who)
        val pk = ContainerOpenPacket()
        pk.windowId = who.getWindowId(this)
        pk.type = getType().networkType
        val holder = this.holder
        pk.x = holder.x.toInt()
        pk.y = holder.y.toInt()
        pk.z = holder.z.toInt()
        who.dataPacket(pk)
        this.sendContents(who)
    }

    override var holder: InventoryHolder?
        get() = super.getHolder() as BlockEntityBeacon
        set(holder) {
            super.holder = holder
        }

    override val blockEntityInventoryHolder: BlockEntityNameable?
        get() = holder
}
