package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PurpurSlab :
    BlockDefinition(
        identifier = "minecraft:purpur_slab",
        states = listOf(CommonStates.minecraftVerticalHalf)
    )
