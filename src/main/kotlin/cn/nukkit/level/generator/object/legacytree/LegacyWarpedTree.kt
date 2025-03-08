package cn.nukkit.level.generator.`object`.legacytree

import cn.nukkit.block.BlockState
import cn.nukkit.block.BlockWarpedStem
import cn.nukkit.block.BlockWarpedWartBlock

class LegacyWarpedTree : LegacyNetherTree() {
    override val trunkBlockState: BlockState
        get() = BlockWarpedStem.PROPERTIES.getDefaultState()

    override val leafBlockState: BlockState
        get() = BlockWarpedWartBlock.PROPERTIES.getDefaultState()
}