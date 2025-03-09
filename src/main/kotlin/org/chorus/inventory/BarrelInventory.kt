package org.chorus.inventory

import cn.nukkit.Player
import cn.nukkit.block.BlockBarrel
import cn.nukkit.blockentity.BlockEntityBarrel
import cn.nukkit.blockentity.BlockEntityNameable
import cn.nukkit.level.Sound
import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType

class BarrelInventory(barrel: BlockEntityBarrel?) : ContainerInventory(barrel, InventoryType.CONTAINER, 27),
    BlockEntityInventoryNameable {
    override fun init() {
        val map = super.slotTypeMap()
        for (i in 0..<getSize()) {
            map!![i] = ContainerSlotType.BARREL
        }
    }

    override var holder: InventoryHolder?
        get() = holder as BlockEntityBarrel
        set(holder) {
            super.holder = holder
        }

    override fun onOpen(who: Player) {
        super.onOpen(who)

        if (getViewers().size == 1) {
            val barrel: BlockEntityBarrel? = this.holder
            val level = barrel!!.level
            if (level != null) {
                val block = barrel.block
                if (block is BlockBarrel) {
                    if (!block.isOpen) {
                        block.isOpen = true
                        level.setBlock(block.position, block, true, true)
                        level.addSound(block.position, Sound.BLOCK_BARREL_OPEN)
                    }
                }
            }
        }
    }

    override fun onClose(who: Player) {
        super.onClose(who)

        if (getViewers().isEmpty()) {
            val barrel: BlockEntityBarrel? = this.holder
            val level = barrel!!.level
            if (level != null) {
                val block = barrel.block
                if (block is BlockBarrel) {
                    if (block.isOpen) {
                        block.isOpen = false
                        level.setBlock(block.position, block, true, true)
                        level.addSound(block.position, Sound.BLOCK_BARREL_CLOSE)
                    }
                }
            }
        }
    }

    override fun canCauseVibration(): Boolean {
        return true
    }

    override val blockEntityInventoryHolder: BlockEntityNameable?
        get() = holder
}
