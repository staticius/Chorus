package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object YellowGlazedTerracotta :
    BlockDefinition(
        identifier = "minecraft:yellow_glazed_terracotta",
        states = listOf(CommonStates.facingDirection)
    )
