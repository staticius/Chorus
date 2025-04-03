package org.chorus.block

import org.chorus.Player
import org.chorus.blockentity.BlockEntityID
import org.chorus.blockentity.BlockEntityMovingBlock
import org.chorus.item.Item
import org.chorus.math.BlockFace
import org.chorus.math.Vector3

class BlockMovingBlock @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockTransparent(blockstate), BlockEntityHolder<BlockEntityMovingBlock> {
    override val name: String
        get() = "MovingBlock"

    override fun getBlockEntityType(): String {
        return BlockEntityID.MOVING_BLOCK
    }

    override fun getBlockEntityClass() = BlockEntityMovingBlock::class.java

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
        return false
    }

    override fun canBePushed(): Boolean {
        return false
    }

    override fun canBePulled(): Boolean {
        return false
    }

    override fun isBreakable(vector: Vector3, layer: Int, face: BlockFace?, item: Item?, player: Player?): Boolean {
        return false
    }

    override fun canPassThrough(): Boolean {
        return true
    }

    override val isSolid: Boolean
        get() = false

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MOVING_BLOCK)

    }
}