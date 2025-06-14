package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object CaveVines :
    BlockDefinition(identifier = "minecraft:cave_vines", states = listOf(CommonStates.growingPlantAge))
