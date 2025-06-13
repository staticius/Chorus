package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object RedGlazedTerracotta :
    BlockDefinition(
        identifier = "minecraft:red_glazed_terracotta",
        states = listOf(CommonStates.facingDirection)
    )
