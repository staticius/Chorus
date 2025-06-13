package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object WitherSkeletonSkull :
    BlockDefinition(
        identifier = "minecraft:wither_skeleton_skull",
        states = listOf(CommonStates.facingDirection)
    )
