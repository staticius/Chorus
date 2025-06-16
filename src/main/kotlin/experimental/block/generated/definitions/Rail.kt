package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Rail : BlockDefinition(
    identifier = "minecraft:rail",
    states = listOf(CommonStates.railDirection10),
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        InternalFrictionComponent(internalFriction = 0.95f),
        MineableComponent(hardness = 0.7f),
        CollisionBoxComponent(enabled = false)
    )
)
