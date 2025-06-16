package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object PaleOakStairs : BlockDefinition(
    identifier = "minecraft:pale_oak_stairs",
    states = listOf(CommonStates.upsideDownBit, CommonStates.weirdoDirection),
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 255, g = 252, b = 245, a = 255),
        FlammableComponent(catchChance = 5, destroyChance = 20),
        MineableComponent(hardness = 2.0f),
        CollisionBoxComponent(origin = IVector3(x = 0, y = 0, z = 0), size = IVector3(x = 16, y = 8, z = 16))
    )
)
