package org.chorus.block

import cn.nukkit.math.BlockFace

/**
 * @author Pub4Game
 * @since 21.02.2016
 */
class BlockSlime : BlockTransparent {
    constructor() : super(Companion.properties.getDefaultState())

    constructor(blockState: BlockState?) : super(blockState)

    override val hardness: Double
        get() = 0.0

    override val name: String
        get() = "Slime Block"

    override val resistance: Double
        get() = 0.0

    override val lightFilter: Int
        get() = 1

    override fun canSticksBlock(): Boolean {
        return true
    }

    override val isSolid: Boolean
        get() = true

    override fun isSolid(side: BlockFace): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SLIME)
            get() = Companion.field
    }
}
