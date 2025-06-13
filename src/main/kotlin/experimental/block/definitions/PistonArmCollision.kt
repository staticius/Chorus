package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PistonArmCollision :
    BlockDefinition(
        identifier = "minecraft:piston_arm_collision",
        states = listOf(CommonStates.facingDirection)
    )
