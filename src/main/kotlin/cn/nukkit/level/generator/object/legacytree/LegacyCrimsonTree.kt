package cn.nukkit.level.generator.`object`.legacytree

import cn.nukkit.block.BlockCrimsonStem
import cn.nukkit.block.BlockNetherWartBlock
import cn.nukkit.block.BlockState

class LegacyCrimsonTree : LegacyNetherTree() {
    override val trunkBlockState: BlockState
        get() = BlockCrimsonStem.PROPERTIES.getDefaultState()

    override val leafBlockState: BlockState
        get() = BlockNetherWartBlock.PROPERTIES.getDefaultState()
}
