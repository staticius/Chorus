package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object AmethystCluster :
    BlockDefinition(
        identifier = "minecraft:amethyst_cluster",
        states = listOf(CommonStates.minecraftBlockFace)
    )
