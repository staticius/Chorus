package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.FlammableComponent
import org.chorus_oss.chorus.experimental.block.components.MineableComponent
import org.chorus_oss.chorus.experimental.block.components.MoveableComponent
import org.chorus_oss.chorus.experimental.block.components.TransparentComponent
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SpruceLeaves : BlockDefinition(
    identifier = "minecraft:spruce_leaves",
    states = listOf(CommonStates.persistentBit, CommonStates.updateBit),
    components = listOf(
        TransparentComponent(transparent = true),
        FlammableComponent(catchChance = 30, destroyChance = 60),
        MineableComponent(hardness = 0.2f),
        MoveableComponent(movement = MoveableComponent.Movement.Break, sticky = false)
    )
)
