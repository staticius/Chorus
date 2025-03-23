package org.chorus.level.generator.`object`.legacytree

import org.chorus.block.BlockCrimsonStem
import org.chorus.block.BlockNetherWartBlock
import org.chorus.block.BlockState

class LegacyCrimsonTree : LegacyNetherTree() {
    override val trunkBlockState: BlockState
        get() = BlockCrimsonStem.properties.defaultState

    override val leafBlockState: BlockState
        get() = BlockNetherWartBlock.properties.defaultState
}
