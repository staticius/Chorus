package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.protocol.types.IVector3

object Conduit : BlockDefinition(
    identifier = "minecraft:conduit",
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 92, g = 219, b = 213, a = 255),
        LightEmissionComponent(emission = 15),
        MineableComponent(hardness = 3.0f),
        CollisionBoxComponent(origin = IVector3(x = 5, y = 5, z = 5), size = IVector3(x = 11, y = 11, z = 11))
    )
)
