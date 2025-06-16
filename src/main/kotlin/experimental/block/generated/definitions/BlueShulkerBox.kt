package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*

object BlueShulkerBox : BlockDefinition(
    identifier = "minecraft:blue_shulker_box",
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        MapColorComponent(r = 51, g = 76, b = 178, a = 255),
        MineableComponent(hardness = 2.0f),
        MoveableComponent(movement = MoveableComponent.Movement.Break, sticky = false)
    )
)
