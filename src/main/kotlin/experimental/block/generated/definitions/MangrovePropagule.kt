package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object MangrovePropagule : BlockDefinition(
    identifier = "minecraft:mangrove_propagule",
    states = listOf(CommonStates.hanging, CommonStates.propaguleStage),
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        MapColorComponent(r = 0, g = 124, b = 0, a = 255),
        InternalFrictionComponent(internalFriction = 0.95f),
        MineableComponent(hardness = 0.0f),
        MoveableComponent(movement = MoveableComponent.Movement.Break, sticky = false),
        CollisionBoxComponent(enabled = false)
    )
)
