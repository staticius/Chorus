package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SoulCampfire : BlockDefinition(
    identifier = "minecraft:soul_campfire",
    states = listOf(CommonStates.extinguished, CommonStates.minecraftCardinalDirection),
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 129, g = 86, b = 49, a = 255),
        LightEmissionComponent(emission = 10),
        MineableComponent(hardness = 5.0f),
        MoveableComponent(movement = MoveableComponent.Movement.Break, sticky = false)
    )
)
