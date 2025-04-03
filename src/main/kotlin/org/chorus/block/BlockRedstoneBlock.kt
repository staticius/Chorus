package org.chorus.block

import org.chorus.Player
import org.chorus.item.Item
import org.chorus.item.ItemTool
import org.chorus.math.BlockFace
import org.chorus.utils.RedstoneComponent.Companion.updateAroundRedstone

class BlockRedstoneBlock @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockSolid(blockstate), RedstoneComponent {
    override val resistance: Double
        get() = 10.0

    override val hardness: Double
        get() = 5.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val name: String
        get() = "Redstone Block"

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun place(
        item: Item?,
        block: Block,
        target: Block?,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        if (super.place(item, block, target, face, fx, fy, fz, player)) {
            updateAroundRedstone()

            return true
        }
        return false
    }

    override fun onBreak(item: Item?): Boolean {
        if (!super.onBreak(item)) {
            return false
        }

        updateAroundRedstone()
        return true
    }

    override val isPowerSource: Boolean
        get() = true

    override fun getWeakPower(face: BlockFace): Int {
        return 15
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.REDSTONE_BLOCK)

    }
}