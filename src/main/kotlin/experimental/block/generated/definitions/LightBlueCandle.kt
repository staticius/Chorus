package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object LightBlueCandle : BlockDefinition(
    identifier = "minecraft:light_blue_candle",
    states = listOf(CommonStates.candles, CommonStates.lit),
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        MapColorComponent(r = 102, g = 153, b = 216, a = 255),
        InternalFrictionComponent(internalFriction = 0.95f),
        MineableComponent(hardness = 0.1f),
        MoveableComponent(movement = MoveableComponent.Movement.Break, sticky = false),
        CollisionBoxComponent(enabled = false)
    )
)
