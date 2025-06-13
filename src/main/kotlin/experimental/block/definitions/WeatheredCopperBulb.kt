package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object WeatheredCopperBulb : BlockDefinition(
    identifier = "minecraft:weathered_copper_bulb",
    states = listOf(
        CommonStates.lit,
        CommonStates.poweredBit
    )
)
