package cn.nukkit.inventory

import cn.nukkit.blockentity.BlockEntityDispenser
import cn.nukkit.blockentity.BlockEntityNameable


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
