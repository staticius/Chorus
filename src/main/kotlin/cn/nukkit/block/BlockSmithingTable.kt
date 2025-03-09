package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.inventory.BlockInventoryHolder
import cn.nukkit.inventory.Inventory
import cn.nukkit.item.Item
import cn.nukkit.item.ItemTool
import cn.nukkit.math.BlockFace
import java.util.function.Supplier

class BlockSmithingTable @JvmOverloads constructor(blockState: BlockState? = Companion.properties.getDefaultState()) :
    BlockSolid(blockState), BlockInventoryHolder {
    override val name: String
        get() = "Smithing Table"

    override fun canBeActivated(): Boolean {
        return true
    }

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        return super.place(item, block, target, face, fx, fy, fz, player)
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
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
            get() = Companion.field
    }
}
