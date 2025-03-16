package org.chorus.block

import org.chorus.Player
import org.chorus.inventory.BlockInventoryHolder
import org.chorus.inventory.CraftingTableInventory
import org.chorus.inventory.Inventory
import org.chorus.item.Item
import org.chorus.item.ItemTool
import org.chorus.math.BlockFace
import java.util.function.Supplier

class BlockCraftingTable @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockSolid(blockState), BlockInventoryHolder {
    override val name: String
        get() = "Crafting Table"

    override fun canBeActivated(): Boolean {
        return true
    }

    override val hardness: Double
        get() = 2.5

    override val resistance: Double
        get() = 15.0

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (player != null) {
            val itemInHand = player.getInventory().itemInHand
            if (player.isSneaking() && !(itemInHand.isTool || itemInHand.isNothing)) {
                return false
            }
            player.addWindow(inventory!!)
        }
        return true
    }

    override fun blockInventorySupplier(): Supplier<Inventory?> {
        return Supplier<Inventory?> { CraftingTableInventory(this) }
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CRAFTING_TABLE)
    }
}
