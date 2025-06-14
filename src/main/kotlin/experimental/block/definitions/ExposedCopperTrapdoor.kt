package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object ExposedCopperTrapdoor : BlockDefinition(
    identifier = "minecraft:exposed_copper_trapdoor",
    states = listOf(CommonStates.direction, CommonStates.openBit, CommonStates.upsideDownBit)
)
