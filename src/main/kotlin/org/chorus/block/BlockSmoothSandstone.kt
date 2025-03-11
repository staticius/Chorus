package org.chorus.block

open class BlockSmoothSandstone @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockSandstone(blockstate) {
    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 6.0

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SMOOTH_SANDSTONE)

    }
}
