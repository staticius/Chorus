package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.CollisionBoxComponent
import org.chorus_oss.chorus.experimental.block.components.MapColorComponent
import org.chorus_oss.chorus.experimental.block.components.MineableComponent
import org.chorus_oss.chorus.experimental.block.components.TransparentComponent
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object Bell : BlockDefinition(
    identifier = "minecraft:bell",
    states = listOf(CommonStates.attachment, CommonStates.direction, CommonStates.toggleBit),
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 250, g = 238, b = 77, a = 255),
        MineableComponent(hardness = 1.0f),
        CollisionBoxComponent(origin = IVector3(x = 4, y = 4, z = 4), size = IVector3(x = 12, y = 16, z = 12))
    )
)
