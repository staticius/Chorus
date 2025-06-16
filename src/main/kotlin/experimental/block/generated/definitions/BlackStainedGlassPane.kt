package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.MineableComponent
import org.chorus_oss.chorus.experimental.block.components.SolidComponent
import org.chorus_oss.chorus.experimental.block.components.TransparentComponent

object BlackStainedGlassPane : BlockDefinition(
    identifier = "minecraft:black_stained_glass_pane",
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        MineableComponent(hardness = 0.3f)
    )
)
