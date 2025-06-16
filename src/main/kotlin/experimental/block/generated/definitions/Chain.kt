package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.CollisionBoxComponent
import org.chorus_oss.chorus.experimental.block.components.MineableComponent
import org.chorus_oss.chorus.experimental.block.components.TransparentComponent
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object Chain : BlockDefinition(
    identifier = "minecraft:chain",
    states = listOf(CommonStates.pillarAxis),
    components = listOf(
        TransparentComponent(transparent = true),
        MineableComponent(hardness = 5.0f),
        CollisionBoxComponent(origin = IVector3(x = 7, y = 0, z = 7), size = IVector3(x = 9, y = 16, z = 9))
    )
)
