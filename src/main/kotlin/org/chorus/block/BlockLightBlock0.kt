package org.chorus.block

import org.chorus.math.AxisAlignedBB

open class BlockLightBlock0 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockTransparent(blockstate) {
    override val name: String
        get() = "Light Block"

    override val boundingBox: AxisAlignedBB?
        get() = null

    override val waterloggingLevel: Int
        get() = 2

    override fun canBeFlowedInto(): Boolean {
        return true
    }

    override fun canBeReplaced(): Boolean {
        return true
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val hardness: Double
        get() = 0.0

    override val resistance: Double
        get() = 0.0

    override fun canPassThrough(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLOCK_0)
            get() = Companion.field
    }
}