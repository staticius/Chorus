package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object CrimsonDoubleSlab :
    BlockDefinition(
        identifier = "minecraft:crimson_double_slab",
        states = listOf(CommonStates.minecraftVerticalHalf)
    )
