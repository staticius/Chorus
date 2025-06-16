package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PaleOakLeaves : BlockDefinition(
    identifier = "minecraft:pale_oak_leaves",
    states = listOf(CommonStates.persistentBit, CommonStates.updateBit),
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 167, g = 167, b = 167, a = 255),
        FlammableComponent(catchChance = 30, destroyChance = 60),
        MineableComponent(hardness = 0.2f),
        MoveableComponent(movement = MoveableComponent.Movement.Break, sticky = false)
    )
)
