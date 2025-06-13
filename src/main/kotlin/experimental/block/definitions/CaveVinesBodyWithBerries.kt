package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object CaveVinesBodyWithBerries : BlockDefinition(
    identifier = "minecraft:cave_vines_body_with_berries",
    states = listOf(CommonStates.growingPlantAge)
)
