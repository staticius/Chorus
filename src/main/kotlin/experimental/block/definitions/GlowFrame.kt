package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object GlowFrame : BlockDefinition(
    identifier = "minecraft:glow_frame",
    states = listOf(
        CommonStates.facingDirection,
        CommonStates.itemFrameMapBit,
        CommonStates.itemFramePhotoBit
    )
)
