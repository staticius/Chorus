package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object FlowingWater : BlockDefinition(
    identifier = "minecraft:flowing_water",
    states = listOf(CommonStates.liquidDepth),
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        InternalFrictionComponent(internalFriction = 0.5f),
        LightDampeningComponent(dampening = 2),
        ReplaceableComponent,
        MineableComponent(hardness = 100.0f),
        MoveableComponent(movement = MoveableComponent.Movement.Break, sticky = false),
        CollisionBoxComponent(enabled = false)
    )
)
