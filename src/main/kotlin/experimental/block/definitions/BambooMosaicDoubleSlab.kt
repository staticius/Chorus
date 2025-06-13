package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object BambooMosaicDoubleSlab : BlockDefinition(
    identifier = "minecraft:bamboo_mosaic_double_slab",
    states = listOf(CommonStates.minecraftVerticalHalf)
)
