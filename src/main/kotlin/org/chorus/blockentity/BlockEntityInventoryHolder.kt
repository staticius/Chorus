package org.chorus.blockentity

import org.chorus.inventory.InventoryHolder

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
