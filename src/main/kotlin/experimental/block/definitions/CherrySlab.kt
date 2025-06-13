package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object CherrySlab :
    BlockDefinition(
        identifier = "minecraft:cherry_slab",
        states = listOf(CommonStates.minecraftVerticalHalf)
    )
