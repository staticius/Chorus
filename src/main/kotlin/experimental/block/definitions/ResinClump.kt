package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object ResinClump :
    BlockDefinition(
        identifier = "minecraft:resin_clump",
        states = listOf(CommonStates.multiFaceDirectionBits)
    )
