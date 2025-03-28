package org.chorus.inventory

import org.chorus.Player
import org.chorus.block.BlockCartographyTable
import org.chorus.item.Item
import org.chorus.network.protocol.ContainerOpenPacket
import org.chorus.network.protocol.types.itemstack.ContainerSlotType

class CartographyTableInventory(blockCartographyTable: BlockCartographyTable?) :
    BaseInventory(blockCartographyTable, InventoryType.CARTOGRAPHY, 2) {
    override fun init() {
        val map = super.networkSlotMap()
        for (i in 0..<getSize()) {
            map[i] = 12 + i
        }

        val map2 = super.slotTypeMap()
        map2[0] = ContainerSlotType.CARTOGRAPHY_INPUT
        map2[1] = ContainerSlotType.CARTOGRAPHY_ADDITIONAL
    }

    override fun onOpen(who: Player) {
        super.onOpen(who)
        who.dataPacket(ContainerOpenPacket(
            containerID = who.getWindowId(this),
            containerType = type.networkType,
            position = holder.vector3.asBlockVector3(),
            targetActorID = who.getId()
        ))
        this.sendContents(who)
    }

    override fun onClose(who: Player) {
        super.onClose(who)

        var drops = arrayOf<Item?>(
            input,
            additional
        )
        drops = who.inventory.addItem(*drops)
        for (drop in drops) {
            if (!who.dropItem(drop)) {
                getHolder().level.dropItem(getHolder().vector3.add(0.5, 0.5, 0.5), drop)
            }
        }

        clear(0)
        clear(1)
    }

    val input: Item
        get() = this.getItem(0)

    val additional: Item
        get() = this.getItem(1)
}
