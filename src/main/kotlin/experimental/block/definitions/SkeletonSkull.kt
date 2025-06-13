package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SkeletonSkull :
    BlockDefinition(
        identifier = "minecraft:skeleton_skull",
        states = listOf(CommonStates.facingDirection)
    )
