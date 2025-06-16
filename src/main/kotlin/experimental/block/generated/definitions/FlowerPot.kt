package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object FlowerPot : BlockDefinition(
    identifier = "minecraft:flower_pot",
    states = listOf(CommonStates.updateBit),
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        MineableComponent(hardness = 0.0f),
        MoveableComponent(movement = MoveableComponent.Movement.Break, sticky = false),
        CollisionBoxComponent(origin = IVector3(x = 5, y = 0, z = 5), size = IVector3(x = 11, y = 6, z = 11))
    )
)
