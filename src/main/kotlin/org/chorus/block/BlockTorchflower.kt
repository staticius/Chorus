package org.chorus.block

import org.chorus.Player
import org.chorus.item.Item
import org.chorus.math.BlockFace

class BlockTorchflower @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockFlower(blockstate) {
    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        return false
    }

    override val name: String
        get() = "Torchflower"

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.TORCHFLOWER)
    }
}