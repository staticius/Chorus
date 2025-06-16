package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object PaleMossCarpet : BlockDefinition(
    identifier = "minecraft:pale_moss_carpet",
    states = listOf(
        CommonStates.paleMossCarpetSideEast,
        CommonStates.paleMossCarpetSideNorth,
        CommonStates.paleMossCarpetSideSouth,
        CommonStates.paleMossCarpetSideWest,
        CommonStates.upperBlockBit
    ),
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        MapColorComponent(r = 153, g = 153, b = 153, a = 255),
        MineableComponent(hardness = 0.1f),
        MoveableComponent(movement = MoveableComponent.Movement.Break, sticky = false),
        CollisionBoxComponent(origin = IVector3(x = 0, y = 0, z = 0), size = IVector3(x = 16, y = 1, z = 16))
    )
)
