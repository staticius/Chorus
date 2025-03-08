package cn.nukkit.blockentity

import cn.nukkit.inventory.InventoryHolder

/**
 * Semantic interface
 */
interface BlockEntityInventoryHolder : BlockEntityNameable, InventoryHolder {
    var inventoryTitle: String?
        get() = name
        set(name) {
            setName(name)
        }
}
