package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object Cauldron : BlockDefinition(
    identifier = "minecraft:cauldron",
    states = listOf(CommonStates.cauldronLiquid, CommonStates.fillLevel),
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 112, g = 112, b = 112, a = 255),
        LightDampeningComponent(dampening = 3),
        MineableComponent(hardness = 2.0f),
        CollisionBoxComponent(origin = IVector3(x = 5, y = 5, z = 5), size = IVector3(x = 11, y = 11, z = 11))
    )
)
