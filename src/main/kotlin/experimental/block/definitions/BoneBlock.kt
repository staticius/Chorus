package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object BoneBlock : BlockDefinition(
    identifier = "minecraft:bone_block",
    states = listOf(CommonStates.deprecated, CommonStates.pillarAxis)
)
