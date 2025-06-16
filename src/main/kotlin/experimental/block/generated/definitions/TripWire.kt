package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.CollisionBoxComponent
import org.chorus_oss.chorus.experimental.block.components.InternalFrictionComponent
import org.chorus_oss.chorus.experimental.block.components.MineableComponent
import org.chorus_oss.chorus.experimental.block.components.TransparentComponent
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object TripWire : BlockDefinition(
    identifier = "minecraft:trip_wire",
    states = listOf(
        CommonStates.attachedBit,
        CommonStates.disarmedBit,
        CommonStates.poweredBit,
        CommonStates.suspendedBit
    ),
    components = listOf(
        TransparentComponent(transparent = true),
        InternalFrictionComponent(internalFriction = 0.95f),
        MineableComponent(hardness = 0.0f),
        CollisionBoxComponent(enabled = false)
    )
)
