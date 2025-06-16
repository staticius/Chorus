package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object CaveVinesHeadWithBerries : BlockDefinition(
    identifier = "minecraft:cave_vines_head_with_berries",
    states = listOf(CommonStates.growingPlantAge),
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        MapColorComponent(r = 0, g = 124, b = 0, a = 255),
        LightEmissionComponent(emission = 14),
        MineableComponent(hardness = 0.0f)
    )
)
