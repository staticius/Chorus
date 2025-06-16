package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object BrewingStand : BlockDefinition(
    identifier = "minecraft:brewing_stand",
    states = listOf(
        CommonStates.brewingStandSlotABit,
        CommonStates.brewingStandSlotBBit,
        CommonStates.brewingStandSlotCBit
    ),
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        MapColorComponent(r = 167, g = 167, b = 167, a = 255),
        LightEmissionComponent(emission = 1),
        MineableComponent(hardness = 0.5f),
        CollisionBoxComponent(origin = IVector3(x = 7, y = 0, z = 7), size = IVector3(x = 9, y = 14, z = 9))
    )
)
