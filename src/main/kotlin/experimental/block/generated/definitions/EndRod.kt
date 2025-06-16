package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.CollisionBoxComponent
import org.chorus_oss.chorus.experimental.block.components.LightEmissionComponent
import org.chorus_oss.chorus.experimental.block.components.MineableComponent
import org.chorus_oss.chorus.experimental.block.components.TransparentComponent
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object EndRod : BlockDefinition(
    identifier = "minecraft:end_rod",
    states = listOf(CommonStates.facingDirection),
    components = listOf(
        TransparentComponent(transparent = true),
        LightEmissionComponent(emission = 14),
        MineableComponent(hardness = 0.0f),
        CollisionBoxComponent(origin = IVector3(x = 6, y = 0, z = 6), size = IVector3(x = 10, y = 16, z = 10))
    )
)
