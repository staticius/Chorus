package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object TurtleEgg : BlockDefinition(
    identifier = "minecraft:turtle_egg",
    states = listOf(CommonStates.crackedState, CommonStates.turtleEggCount),
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        MapColorComponent(r = 247, g = 233, b = 163, a = 255),
        MineableComponent(hardness = 0.5f),
        MoveableComponent(movement = MoveableComponent.Movement.Break, sticky = false),
        CollisionBoxComponent(origin = IVector3(x = 3, y = 0, z = 3), size = IVector3(x = 12, y = 7, z = 12))
    )
)
