package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object PoweredComparator : BlockDefinition(
    identifier = "minecraft:powered_comparator",
    states = listOf(CommonStates.minecraftCardinalDirection, CommonStates.outputLitBit, CommonStates.outputSubtractBit),
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        MineableComponent(hardness = 0.0f),
        MoveableComponent(movement = MoveableComponent.Movement.Break, sticky = false),
        CollisionBoxComponent(origin = IVector3(x = 0, y = 0, z = 0), size = IVector3(x = 16, y = 2, z = 16))
    )
)
