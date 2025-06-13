package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object CaveVinesHeadWithBerries : BlockDefinition(
    identifier = "minecraft:cave_vines_head_with_berries",
    states = listOf(CommonStates.growingPlantAge)
)
