package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.MapColorComponent
import org.chorus_oss.chorus.experimental.block.components.MineableComponent
import org.chorus_oss.chorus.experimental.block.components.TransparentComponent

object LightBlueStainedGlass : BlockDefinition(
    identifier = "minecraft:light_blue_stained_glass",
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 102, g = 153, b = 216, a = 255),
        MineableComponent(hardness = 0.3f)
    )
)
