package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*

object CopperGrate : BlockDefinition(
    identifier = "minecraft:copper_grate",
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        MapColorComponent(r = 216, g = 127, b = 51, a = 255),
        MineableComponent(hardness = 3.0f),
        MoveableComponent(movement = MoveableComponent.Movement.Both, sticky = false),
        CollisionBoxComponent(enabled = false)
    )
)
