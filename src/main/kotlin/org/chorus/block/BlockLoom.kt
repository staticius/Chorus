package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.inventory.BlockInventoryHolder
import cn.nukkit.inventory.Inventory
import cn.nukkit.inventory.LoomInventory
import cn.nukkit.item.*
import cn.nukkit.math.BlockFace
import cn.nukkit.math.BlockFace.Companion.horizontals
import cn.nukkit.utils.Faceable
import java.util.function.Supplier

/**
 * @implNote Faceable since FUTURE
 */
class BlockLoom @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockState), Faceable, BlockInventoryHolder {
    override val name: String
        get() = "Loom"

    override fun toItem(): Item? {
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
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (player != null) {
            val itemInHand = player.getInventory().itemInHand
            if (player.isSneaking() && !(itemInHand.isTool || itemInHand.isNull)) {
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
            blockFace = player.getDirection()!!.getOpposite()!!
        }
        level.setBlock(this.position, this, true, true)
        return true
    }

    override var blockFace: BlockFace
        get() = horizontals[getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.DIRECTION)]!!
        set(face) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.DIRECTION, face.horizontalIndex)
        }

    override fun blockInventorySupplier(): Supplier<Inventory?> {
        return Supplier { LoomInventory(this) }
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LOOM, CommonBlockProperties.DIRECTION)
            get() = Companion.field
    }
}
