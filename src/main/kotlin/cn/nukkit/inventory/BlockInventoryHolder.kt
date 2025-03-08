package cn.nukkit.inventory

import cn.nukkit.block.Block
import java.util.function.Supplier

interface BlockInventoryHolder : InventoryHolder {
    fun blockInventorySupplier(): Supplier<Inventory?>

    val block: Block
        get() = this as Block

    override val inventory: Inventory?
        get() = blockInventorySupplier().get()

    companion object {
        const val KEY: String = "inventory"
    }
}
