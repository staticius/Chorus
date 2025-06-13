package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object TrialSpawner : BlockDefinition(
    identifier = "minecraft:trial_spawner",
    states = listOf(
        CommonStates.ominous,
        CommonStates.trialSpawnerState
    )
)
