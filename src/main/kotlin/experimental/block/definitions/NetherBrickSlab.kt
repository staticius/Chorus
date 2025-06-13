package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object NetherBrickSlab :
    BlockDefinition(
        identifier = "minecraft:nether_brick_slab",
        states = listOf(CommonStates.minecraftVerticalHalf)
    )
