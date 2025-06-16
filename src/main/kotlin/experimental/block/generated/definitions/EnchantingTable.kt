package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.protocol.types.IVector3

object EnchantingTable : BlockDefinition(
    identifier = "minecraft:enchanting_table",
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 153, g = 51, b = 51, a = 255),
        LightEmissionComponent(emission = 7),
        MineableComponent(hardness = 5.0f),
        CollisionBoxComponent(origin = IVector3(x = 0, y = 0, z = 0), size = IVector3(x = 16, y = 12, z = 16))
    )
)
