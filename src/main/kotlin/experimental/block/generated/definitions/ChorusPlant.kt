package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.MapColorComponent
import org.chorus_oss.chorus.experimental.block.components.MineableComponent
import org.chorus_oss.chorus.experimental.block.components.MoveableComponent
import org.chorus_oss.chorus.experimental.block.components.TransparentComponent

object ChorusPlant : BlockDefinition(
    identifier = "minecraft:chorus_plant",
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 153, g = 90, b = 205, a = 255),
        MineableComponent(hardness = 0.4f),
        MoveableComponent(movement = MoveableComponent.Movement.Break, sticky = false)
    )
)
