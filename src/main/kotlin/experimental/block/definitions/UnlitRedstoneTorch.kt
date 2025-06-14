package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object UnlitRedstoneTorch :
    BlockDefinition(identifier = "minecraft:unlit_redstone_torch", states = listOf(CommonStates.torchFacingDirection))
