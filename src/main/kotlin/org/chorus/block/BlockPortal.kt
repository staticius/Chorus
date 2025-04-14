package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemBlock
import org.chorus.math.AxisAlignedBB
import org.chorus.math.BlockFace
import org.chorus.math.BlockFace.Companion.fromHorizontalIndex
import org.chorus.math.Vector3
import org.chorus.utils.Faceable

/**
 * Alias NetherPortal
 */
class BlockPortal @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockFlowable(blockstate), Faceable {
    override val name: String
        get() = "Nether Portal Block"

    override fun canBeFlowedInto(): Boolean {
        return false
    }

    override fun isBreakable(vector: Vector3, layer: Int, face: BlockFace?, item: Item?, player: Player?): Boolean {
        return player != null && player.isCreative
    }

    override val hardness: Double
        get() = -1.0

    override val lightLevel: Int
        get() = 11

    override fun toItem(): Item {
        return ItemBlock(get(BlockID.AIR),)
    }

    override fun canBePushed(): Boolean {
        return false
    }

    override fun canBePulled(): Boolean {
        return false
    }

    override fun onBreak(item: Item?): Boolean {
        var result = super.onBreak(item)
        for (face in BlockFace.entries) {
            val b = this.getSide(face)
            if (b != null) {
                if (b is BlockPortal) {
                    result = result and b.onBreak(item)
                }
            }
        }
        return result
    }

    override fun hasEntityCollision(): Boolean {
        return true
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun recalculateBoundingBox(): AxisAlignedBB {
        return this
    }

    override var blockFace: BlockFace
        get() = fromHorizontalIndex(blockState.specialValue().toInt() and 0x07)
        set(blockFace) {
            throw UnsupportedOperationException()
        }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PORTAL, CommonBlockProperties.PORTAL_AXIS)
    }
}