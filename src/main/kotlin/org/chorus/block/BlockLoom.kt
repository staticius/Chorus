package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.inventory.BlockInventoryHolder
import org.chorus.inventory.Inventory
import org.chorus.inventory.LoomInventory
import org.chorus.item.Item
import org.chorus.item.ItemBlock
import org.chorus.item.ItemTool
import org.chorus.math.BlockFace
import org.chorus.math.BlockFace.Companion.horizontals
import org.chorus.utils.Faceable
import java.util.function.Supplier

/**
 * @implNote Faceable since FUTURE
 */
class BlockLoom @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockSolid(blockState), Faceable, BlockInventoryHolder {
    override val name: String
        get() = "Loom"

    override fun toItem(): Item {
        return ItemBlock(BlockLoom())
    }

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override val resistance: Double
        get() = 12.5

    override val hardness: Double
        get() = 2.5

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
        if (player != null) {
            val itemInHand = player.getInventory().itemInHand
            if (player.isSneaking() && !(itemInHand.isTool || itemInHand.isNothing)) {
                return false
            }
            player.addWindow(inventory!!)
        }
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
        if (player != null) {
            blockFace = player.getDirection().getOpposite()
        }
        level.setBlock(this.position, this, true, true)
        return true
    }

    override var blockFace: BlockFace
        get() = horizontals[getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.DIRECTION)]
        set(face) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.DIRECTION, face.horizontalIndex)
        }

    override fun blockInventorySupplier(): Supplier<Inventory?> {
        return Supplier { LoomInventory(this) }
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LOOM, CommonBlockProperties.DIRECTION)
    }
}
