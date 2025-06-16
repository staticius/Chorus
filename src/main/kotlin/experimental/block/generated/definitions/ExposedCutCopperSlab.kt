package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.CollisionBoxComponent
import org.chorus_oss.chorus.experimental.block.components.MapColorComponent
import org.chorus_oss.chorus.experimental.block.components.MineableComponent
import org.chorus_oss.chorus.experimental.block.components.TransparentComponent
import org.chorus_oss.chorus.experimental.block.state.CommonStates
import org.chorus_oss.protocol.types.IVector3

object ExposedCutCopperSlab : BlockDefinition(
    identifier = "minecraft:exposed_cut_copper_slab",
    states = listOf(CommonStates.minecraftVerticalHalf),
    components = listOf(
        TransparentComponent(transparent = true),
        MapColorComponent(r = 135, g = 107, b = 98, a = 255),
        MineableComponent(hardness = 3.0f),
        CollisionBoxComponent(origin = IVector3(x = 0, y = 0, z = 0), size = IVector3(x = 16, y = 8, z = 16))
    )
)
