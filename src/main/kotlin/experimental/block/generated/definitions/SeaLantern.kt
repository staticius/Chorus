package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.LightEmissionComponent
import org.chorus_oss.chorus.experimental.block.components.MapColorComponent
import org.chorus_oss.chorus.experimental.block.components.MineableComponent
import org.chorus_oss.chorus.experimental.block.components.TransparentComponent

object SeaLantern : BlockDefinition(
    identifier = "minecraft:sea_lantern",
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 255, g = 252, b = 245, a = 255),
        LightEmissionComponent(emission = 15),
        MineableComponent(hardness = 0.3f)
    )
)
