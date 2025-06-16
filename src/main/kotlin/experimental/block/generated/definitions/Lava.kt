package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Lava : BlockDefinition(
    identifier = "minecraft:lava",
    states = listOf(CommonStates.liquidDepth),
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        MapColorComponent(r = 255, g = 0, b = 0, a = 255),
        InternalFrictionComponent(internalFriction = 0.3f),
        LightEmissionComponent(emission = 15),
        LightDampeningComponent(dampening = 2),
        ReplaceableComponent,
        MineableComponent(hardness = 100.0f),
        MoveableComponent(movement = MoveableComponent.Movement.Break, sticky = false),
        CollisionBoxComponent(enabled = false)
    )
)
