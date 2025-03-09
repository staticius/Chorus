package org.chorus.block

import cn.nukkit.math.BlockFace

class BlockSmoothBasalt @JvmOverloads constructor(blockState: BlockState? = Companion.properties.getDefaultState()) :
    BlockBasalt(blockState) {
    override val name: String
        get() = "Smooth Basalt"

    override var pillarAxis: BlockFace.Axis?
        get() =// ignore
            null
        set(axis) {
            // ignore
        }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SMOOTH_BASALT)
            get() = Companion.field
    }
}
