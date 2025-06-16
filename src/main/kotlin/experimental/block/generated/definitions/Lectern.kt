package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.CollisionBoxComponent
import org.chorus_oss.chorus.experimental.block.components.MapColorComponent
import org.chorus_oss.chorus.experimental.block.components.MineableComponent
import org.chorus_oss.chorus.experimental.block.components.TransparentComponent
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object Lectern : BlockDefinition(
    identifier = "minecraft:lectern",
    states = listOf(CommonStates.minecraftCardinalDirection, CommonStates.poweredBit),
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 143, g = 119, b = 72, a = 255),
        MineableComponent(hardness = 2.5f),
        CollisionBoxComponent(origin = IVector3(x = 0, y = 0, z = 0), size = IVector3(x = 16, y = 14, z = 16))
    )
)
