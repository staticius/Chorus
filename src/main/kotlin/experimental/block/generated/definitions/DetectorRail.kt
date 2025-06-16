package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object DetectorRail : BlockDefinition(
    identifier = "minecraft:detector_rail",
    states = listOf(CommonStates.railDataBit, CommonStates.railDirection6),
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        InternalFrictionComponent(internalFriction = 0.95f),
        MineableComponent(hardness = 0.7f),
        CollisionBoxComponent(enabled = false)
    )
)
