package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object AzaleaLeavesFlowered : BlockDefinition(
    identifier = "minecraft:azalea_leaves_flowered",
    states = listOf(CommonStates.persistentBit, CommonStates.updateBit)
)
