package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object GlowLichen :
    BlockDefinition(identifier = "minecraft:glow_lichen", states = listOf(CommonStates.multiFaceDirectionBits))
