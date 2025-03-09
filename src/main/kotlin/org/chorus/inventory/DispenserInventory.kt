package org.chorus.inventory

import org.chorus.blockentity.BlockEntityDispenser
import org.chorus.blockentity.BlockEntityNameable


class DispenserInventory(blockEntity: BlockEntityDispenser?) :
    EjectableInventory(blockEntity, InventoryType.DISPENSER, 9) {
    override var holder: InventoryHolder?
        get() = super.getHolder() as BlockEntityDispenser
        set(holder) {
            super.holder = holder
        }

    override val blockEntityInventoryHolder: BlockEntityNameable?
        get() = holder
}
