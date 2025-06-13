package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PetrifiedOakSlab :
    BlockDefinition(
        identifier = "minecraft:petrified_oak_slab",
        states = listOf(CommonStates.minecraftVerticalHalf)
    )
