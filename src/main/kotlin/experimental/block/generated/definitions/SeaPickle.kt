package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SeaPickle : BlockDefinition(
    identifier = "minecraft:sea_pickle",
    states = listOf(CommonStates.clusterCount, CommonStates.deadBit),
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        MapColorComponent(r = 102, g = 127, b = 51, a = 255),
        InternalFrictionComponent(internalFriction = 0.95f),
        LightEmissionComponent(emission = 6),
        MineableComponent(hardness = 0.0f),
        MoveableComponent(movement = MoveableComponent.Movement.Break, sticky = false),
        CollisionBoxComponent(enabled = false)
    )
)
