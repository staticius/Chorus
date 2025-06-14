package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SculkVein :
    BlockDefinition(identifier = "minecraft:sculk_vein", states = listOf(CommonStates.multiFaceDirectionBits))
