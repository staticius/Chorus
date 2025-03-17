package org.chorus.inventory

import org.chorus.Player
import org.chorus.blockentity.BlockEntityShulkerBox
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.network.protocol.BlockEventPacket
import org.chorus.network.protocol.types.itemstack.ContainerSlotType
import org.chorus.tags.BlockTags

/**
 * @author PetteriM1
 */
class ShulkerBoxInventory(box: BlockEntityShulkerBox?) : ContainerInventory(box, InventoryType.CONTAINER, 27) {
    override var holder: InventoryHolder?
        get() = holder as BlockEntityShulkerBox
        set(holder) {
            super.holder = holder
        }

    override fun init() {
        val map = super.slotTypeMap()
        for (i in 0..<getSize()) {
            map[i] = ContainerSlotType.SHULKER_BOX
        }
    }

    override fun onOpen(who: Player) {
        super.onOpen(who)

        if (getViewers().size == 1) {
            val pk = BlockEventPacket()
            pk.x = holder.vector3.x.toInt()
            pk.y = holder.vector3.y.toInt()
            pk.z = holder.vector3.z.toInt()
            pk.type = 1
            pk.value = 2

            val level = holder.level
            if (level != null) {
                level.addSound(holder.position.add(0.5, 0.5, 0.5), Sound.RANDOM_SHULKERBOXOPEN)
                level.addChunkPacket(
                    holder.vector3.x.toInt() shr 4,
                    holder.vector3.z.toInt() shr 4, pk
                )
            }
        }
    }

    override fun onClose(who: Player) {
        if (getViewers().size == 1) {
            val pk = BlockEventPacket()
            pk.x = holder.vector3.x.toInt()
            pk.y = holder.vector3.y.toInt()
            pk.z = holder.vector3.z.toInt()
            pk.type = 1
            pk.value = 0

            val level = holder.level
            if (level != null) {
                level.addSound(holder.position.add(0.5, 0.5, 0.5), Sound.RANDOM_SHULKERBOXCLOSED)
                level.addChunkPacket(
                    holder.vector3.x.toInt() shr 4,
                    holder.vector3.z.toInt() shr 4, pk
                )
            }
        }

        super.onClose(who)
    }

    override fun canAddItem(item: Item): Boolean {
        if (item.isBlock && item.blockUnsafe.`is`(BlockTags.PNX_SHULKERBOX)) {
            // Do not allow nested shulker boxes.
            return false
        }
        return super.canAddItem(item)
    }

    override fun canCauseVibration(): Boolean {
        return true
    }
}
