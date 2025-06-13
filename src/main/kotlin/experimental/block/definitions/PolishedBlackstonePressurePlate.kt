package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PolishedBlackstonePressurePlate : BlockDefinition(
    identifier = "minecraft:polished_blackstone_pressure_plate",
    states = listOf(CommonStates.redstoneSignal)
)
