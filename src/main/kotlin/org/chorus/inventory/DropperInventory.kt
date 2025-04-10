package org.chorus.inventory

import org.chorus.blockentity.BlockEntityDropper
import org.chorus.blockentity.BlockEntityNameable


class DropperInventory(blockEntity: BlockEntityDropper) : EjectableInventory(blockEntity, InventoryType.DROPPER, 9) {
    override val blockEntityInventoryHolder: BlockEntityNameable
        get() = holder as BlockEntityDropper
}
