package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object EnderChest : BlockDefinition(
    identifier = "minecraft:ender_chest",
    states = listOf(CommonStates.minecraftCardinalDirection),
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 112, g = 112, b = 112, a = 255),
        LightEmissionComponent(emission = 7),
        MineableComponent(hardness = 22.5f),
        MoveableComponent(movement = MoveableComponent.Movement.None, sticky = false),
        CollisionBoxComponent(origin = IVector3(x = 1, y = 0, z = 1), size = IVector3(x = 15, y = 15, z = 15))
    )
)
