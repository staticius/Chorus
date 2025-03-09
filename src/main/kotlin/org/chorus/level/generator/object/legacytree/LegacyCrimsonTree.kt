package org.chorus.level.generator.`object`.legacytree

import cn.nukkit.block.BlockCrimsonStem
import cn.nukkit.block.BlockNetherWartBlock
import cn.nukkit.block.BlockState

class LegacyCrimsonTree : LegacyNetherTree() {
    override val trunkBlockState: BlockState
        get() = BlockCrimsonStem.properties.getDefaultState()

    override val leafBlockState: BlockState
        get() = BlockNetherWartBlock.properties.getDefaultState()
}
