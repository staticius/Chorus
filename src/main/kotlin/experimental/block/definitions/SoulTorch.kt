package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SoulTorch :
    BlockDefinition(
        identifier = "minecraft:soul_torch",
        states = listOf(CommonStates.torchFacingDirection)
    )
