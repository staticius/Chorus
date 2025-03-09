package org.chorus.inventory

import cn.nukkit.blockentity.BlockEntityDropper
import cn.nukkit.blockentity.BlockEntityNameable


class DropperInventory(blockEntity: BlockEntityDropper?) : EjectableInventory(blockEntity, InventoryType.DROPPER, 9) {
    override var holder: InventoryHolder?
        get() = super.getHolder() as BlockEntityDropper
        set(holder) {
            super.holder = holder
        }

    override val blockEntityInventoryHolder: BlockEntityNameable?
        get() = holder
}
