package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object BlackGlazedTerracotta :
    BlockDefinition(
        identifier = "minecraft:black_glazed_terracotta",
        states = listOf(CommonStates.facingDirection)
    )
