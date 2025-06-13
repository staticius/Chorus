package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Jigsaw : BlockDefinition(
    identifier = "minecraft:jigsaw",
    states = listOf(
        CommonStates.facingDirection,
        CommonStates.rotation
    )
)
