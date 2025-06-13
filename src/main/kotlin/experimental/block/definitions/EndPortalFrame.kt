package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object EndPortalFrame : BlockDefinition(
    identifier = "minecraft:end_portal_frame",
    states = listOf(
        CommonStates.minecraftCardinalDirection,
        CommonStates.endPortalEyeBit
    )
)
