package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PrismarineSlab :
    BlockDefinition(
        identifier = "minecraft:prismarine_slab",
        states = listOf(CommonStates.minecraftVerticalHalf)
    )
