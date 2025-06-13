package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object DarkOakSlab :
    BlockDefinition(
        identifier = "minecraft:dark_oak_slab",
        states = listOf(CommonStates.minecraftVerticalHalf)
    )
