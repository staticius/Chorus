package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.MapColorComponent
import org.chorus_oss.chorus.experimental.block.components.MineableComponent
import org.chorus_oss.chorus.experimental.block.components.SolidComponent
import org.chorus_oss.chorus.experimental.block.components.TransparentComponent

object SporeBlossom : BlockDefinition(
    identifier = "minecraft:spore_blossom",
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        MapColorComponent(r = 0, g = 124, b = 0, a = 255),
        MineableComponent(hardness = 0.0f)
    )
)
