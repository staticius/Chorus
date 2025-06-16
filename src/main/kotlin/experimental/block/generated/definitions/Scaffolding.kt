package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object Scaffolding : BlockDefinition(
    identifier = "minecraft:scaffolding",
    states = listOf(CommonStates.stability, CommonStates.stabilityCheck),
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        MapColorComponent(r = 247, g = 233, b = 163, a = 255),
        FlammableComponent(catchChance = 60, destroyChance = 60),
        MineableComponent(hardness = 0.5f),
        CollisionBoxComponent(origin = IVector3(x = 0, y = 14, z = 0), size = IVector3(x = 16, y = 16, z = 16))
    )
)
