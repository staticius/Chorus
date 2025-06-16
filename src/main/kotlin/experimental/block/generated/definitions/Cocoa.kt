package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object Cocoa : BlockDefinition(
    identifier = "minecraft:cocoa",
    states = listOf(CommonStates.age3, CommonStates.direction),
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 0, g = 124, b = 0, a = 255),
        MineableComponent(hardness = 0.2f),
        MoveableComponent(movement = MoveableComponent.Movement.Break, sticky = false),
        CollisionBoxComponent(origin = IVector3(x = 6, y = 7, z = 11), size = IVector3(x = 10, y = 12, z = 15))
    )
)
