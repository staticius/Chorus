package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object RedstoneTorch :
    BlockDefinition(
        identifier = "minecraft:redstone_torch",
        states = listOf(CommonStates.torchFacingDirection)
    )
