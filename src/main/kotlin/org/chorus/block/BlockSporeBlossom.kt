package org.chorus.block

import org.chorus.Player
import org.chorus.item.Item
import org.chorus.math.BlockFace

class BlockSporeBlossom @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockTransparent(blockstate) {
    override val name: String
        get() = "Spore Blossom"

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
        if (target.isSolid && face == BlockFace.DOWN) {
            return super.place(item, block, target, face, fx, fy, fz, player)
        }
        return false
    }

    override val isSolid: Boolean
        get() = false

    override val hardness: Double
        get() = 0.0

    override val resistance: Double
        get() = 0.0

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SPORE_BLOSSOM)
            get() = Companion.field
    }
}
