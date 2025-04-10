package org.chorus.inventory

import org.chorus.blockentity.BlockEntityDispenser
import org.chorus.blockentity.BlockEntityNameable


class DispenserInventory(blockEntity: BlockEntityDispenser) : EjectableInventory(blockEntity, InventoryType.DISPENSER, 9) {
    override val blockEntityInventoryHolder: BlockEntityNameable
        get() = holder as BlockEntityDispenser
}
