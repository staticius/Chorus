package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.math.BlockFace

class BlockPaleHangingMoss @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHanging(blockstate) {
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
        return isSupportValid && super.place(item, block, target, face, fx, fy, fz, player)
    }

    override val isSupportValid: Boolean
        get() = up()!!.isSolid || up() is BlockPaleHangingMoss

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PALE_HANGING_MOSS, CommonBlockProperties.TIP)

    }
}