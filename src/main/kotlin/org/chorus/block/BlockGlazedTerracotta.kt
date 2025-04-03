package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.item.Item
import org.chorus.item.ItemTool
import org.chorus.math.BlockFace
import org.chorus.math.BlockFace.Companion.fromIndex
import org.chorus.utils.Faceable

abstract class BlockGlazedTerracotta(blockState: BlockState) : BlockSolid(blockState), Faceable {
    override val resistance: Double
        get() = 7.0

    override val hardness: Double
        get() = 1.4

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

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
        val faces = intArrayOf(2, 5, 3, 4)
        this.blockFace = fromIndex(faces[if (player != null) player.getDirection().horizontalIndex else 0])
        return level.setBlock(block.position, this, true, true)
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override var blockFace: BlockFace
        get() = fromIndex(getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION))
        set(face) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION, face.index)
        }

    //带釉陶瓦可以被推动但不能被收回
    //see: https://zh.minecraft.wiki/w/%E5%B8%A6%E9%87%89%E9%99%B6%E7%93%A6
    override fun canBePushed(): Boolean {
        return true
    }

    override fun canBePulled(): Boolean {
        return false
    }
}
