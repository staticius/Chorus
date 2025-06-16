package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*

object Air : BlockDefinition(
    identifier = "minecraft:air",
    components = listOf(
        SolidComponent(solid = false),
        TransparentComponent(transparent = true),
        InternalFrictionComponent(internalFriction = 0.95f),
        ReplaceableComponent,
        MineableComponent(hardness = 0.0f),
        CollisionBoxComponent(enabled = false)
    )
)
