package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object DarkOakFenceGate : BlockDefinition(
    identifier = "minecraft:dark_oak_fence_gate",
    states = listOf(CommonStates.inWallBit, CommonStates.minecraftCardinalDirection, CommonStates.openBit),
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 102, g = 76, b = 51, a = 255),
        FlammableComponent(catchChance = 5, destroyChance = 20),
        MineableComponent(hardness = 2.0f),
        CollisionBoxComponent(origin = IVector3(x = 0, y = 0, z = 6), size = IVector3(x = 16, y = 16, z = 10))
    )
)
