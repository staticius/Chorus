package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SpruceLeaves : BlockDefinition(
    identifier = "minecraft:spruce_leaves",
    states = listOf(CommonStates.persistentBit, CommonStates.updateBit)
)
