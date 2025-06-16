package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.CollisionBoxComponent
import org.chorus_oss.chorus.experimental.block.components.MapColorComponent
import org.chorus_oss.chorus.experimental.block.components.MineableComponent
import org.chorus_oss.chorus.experimental.block.components.TransparentComponent
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object CrimsonFenceGate : BlockDefinition(
    identifier = "minecraft:crimson_fence_gate",
    states = listOf(CommonStates.inWallBit, CommonStates.minecraftCardinalDirection, CommonStates.openBit),
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 148, g = 63, b = 97, a = 255),
        MineableComponent(hardness = 2.0f),
        CollisionBoxComponent(origin = IVector3(x = 0, y = 0, z = 6), size = IVector3(x = 16, y = 16, z = 10))
    )
)
