package org.chorus.block

import org.chorus.Player
import org.chorus.inventory.BlockInventoryHolder
import org.chorus.inventory.Inventory
import org.chorus.item.Item
import org.chorus.item.ItemTool
import org.chorus.math.BlockFace
import java.util.function.Supplier

class BlockSmithingTable @JvmOverloads constructor(blockState: BlockState = Companion.properties.getDefaultState()) :
    BlockSolid(blockState), BlockInventoryHolder {
    override val name: String
        get() = "Smithing Table"

    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (player == null) {
            return false
        }

        player.addWindow(inventory!!)
        return true
    }

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override val resistance: Double
        get() = 12.5

    override val hardness: Double
        get() = 2.5

    override val burnChance: Int
        get() = 5

    override fun canHarvestWithHand(): Boolean {
        return true
    }

    override fun blockInventorySupplier(): Supplier<Inventory?> {
        return Supplier<Inventory?> { SmithingInventory(this) }
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SMITHING_TABLE)

    }
}
