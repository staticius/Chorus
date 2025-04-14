package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.item.Item
import org.chorus.item.ItemBlock
import org.chorus.item.ItemTool
import org.chorus.math.BlockFace
import org.chorus.math.BlockFace.Companion.fromIndex
import org.chorus.utils.Faceable

class BlockEndRod @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    BlockTransparent(blockState), Faceable {
    override val name: String
        get() = "End Rod"

    override val hardness: Double
        get() = 0.0

    override val resistance: Double
        get() = 0.0

    override val lightLevel: Int
        get() = 14

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override var minX: Double
        get() = position.x + 0.4
        set(minX) {
            super.minX = minX
        }

    override var minZ: Double
        get() = position.z + 0.4
        set(minZ) {
            super.minZ = minZ
        }

    override var maxX: Double
        get() = position.x + 0.6
        set(maxX) {
            super.maxX = maxX
        }

    override var maxZ: Double
        get() = position.z + 0.6
        set(maxZ) {
            super.maxZ = maxZ
        }

    override val waterloggingLevel: Int
        get() = 2

    override fun canBeFlowedInto(): Boolean {
        return false
    }

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
        val faces = intArrayOf(0, 1, 3, 2, 5, 4)
        setPropertyValue<Int, IntPropertyType>(
            CommonBlockProperties.FACING_DIRECTION,
            faces[if (player != null) face.index else 0]
        )
        level.setBlock(block.position, this, true, true)

        return true
    }

    override fun toItem(): Item {
        return ItemBlock(this.blockState, name, 0)
    }

    override var blockFace: BlockFace
        get() = fromIndex(getPropertyValue(CommonBlockProperties.FACING_DIRECTION) and 0x07)
        set(blockFace) {}

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.END_ROD, CommonBlockProperties.FACING_DIRECTION)
    }
}
