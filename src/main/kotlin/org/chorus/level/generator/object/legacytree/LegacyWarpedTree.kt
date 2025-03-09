package org.chorus.level.generator.`object`.legacytree

import org.chorus.block.BlockState
import org.chorus.block.BlockWarpedStem
import org.chorus.block.BlockWarpedWartBlock

class LegacyWarpedTree : LegacyNetherTree() {
    override val trunkBlockState: BlockState
        get() = BlockWarpedStem.properties.getDefaultState()

    override val leafBlockState: BlockState
        get() = BlockWarpedWartBlock.properties.getDefaultState()
}