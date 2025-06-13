package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object StickyPistonArmCollision :
    BlockDefinition(
        identifier = "minecraft:sticky_piston_arm_collision",
        states = listOf(CommonStates.facingDirection)
    )
