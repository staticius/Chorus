package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object Lantern : BlockDefinition(
    identifier = "minecraft:lantern",
    states = listOf(CommonStates.hanging),
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        MapColorComponent(r = 167, g = 167, b = 167, a = 255),
        LightEmissionComponent(emission = 15),
        MineableComponent(hardness = 3.5f),
        MoveableComponent(movement = MoveableComponent.Movement.Break, sticky = false),
        CollisionBoxComponent(origin = IVector3(x = 5, y = 0, z = 5), size = IVector3(x = 11, y = 7, z = 11))
    )
)
