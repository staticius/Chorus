package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.CollisionBoxComponent
import org.chorus_oss.chorus.experimental.block.components.MapColorComponent
import org.chorus_oss.chorus.experimental.block.components.MineableComponent
import org.chorus_oss.chorus.experimental.block.components.TransparentComponent
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object Grindstone : BlockDefinition(
    identifier = "minecraft:grindstone",
    states = listOf(CommonStates.attachment, CommonStates.direction),
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 167, g = 167, b = 167, a = 255),
        MineableComponent(hardness = 2.0f),
        CollisionBoxComponent(origin = IVector3(x = 2, y = 2, z = 2), size = IVector3(x = 14, y = 16, z = 14))
    )
)
