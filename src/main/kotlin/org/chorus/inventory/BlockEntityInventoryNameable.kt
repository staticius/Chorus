package org.chorus.inventory

import cn.nukkit.blockentity.BlockEntityNameable

interface BlockEntityInventoryNameable : InventoryNameable {
    val blockEntityInventoryHolder: BlockEntityNameable?

    override var inventoryTitle: String
        get() = blockEntityInventoryHolder!!.name!!
        set(name) {
            blockEntityInventoryHolder!!.name = name
        }
}
