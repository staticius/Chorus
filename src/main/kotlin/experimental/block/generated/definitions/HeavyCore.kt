package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.protocol.types.IVector3

object HeavyCore : BlockDefinition(
    identifier = "minecraft:heavy_core",
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        MapColorComponent(r = 167, g = 167, b = 167, a = 255),
        MoveableComponent(movement = MoveableComponent.Movement.Both, sticky = false),
        CollisionBoxComponent(origin = IVector3(x = 4, y = 0, z = 4), size = IVector3(x = 12, y = 8, z = 12))
    )
)
