package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.MapColorComponent
import org.chorus_oss.chorus.experimental.block.components.MineableComponent
import org.chorus_oss.chorus.experimental.block.components.TransparentComponent

object TintedGlass : BlockDefinition(
    identifier = "minecraft:tinted_glass",
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 76, g = 76, b = 76, a = 255),
        MineableComponent(hardness = 0.3f)
    )
)
