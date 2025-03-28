package org.chorus.inventory

import org.chorus.Player
import org.chorus.block.BlockLoom
import org.chorus.item.Item
import org.chorus.network.protocol.ContainerOpenPacket
import org.chorus.network.protocol.types.itemstack.ContainerSlotType

class LoomInventory(blockLoom: BlockLoom?) : BaseInventory(blockLoom, InventoryType.LOOM, 3) {
    override fun init() {
        val map = super.networkSlotMap()
        for (i in 0..<getSize()) {
            map[i] = 9 + i
        }

        val map2 = super.slotTypeMap()
        map2[0] = ContainerSlotType.LOOM_INPUT
        map2[1] = ContainerSlotType.LOOM_DYE
        map2[2] = ContainerSlotType.LOOM_MATERIAL
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

    val banner: Item
        get() = getItem(0)

    val dye: Item
        get() = getItem(1)

    val pattern: Item
        get() = getItem(2)
}
