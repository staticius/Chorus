package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object EnderChest :
    BlockDefinition(identifier = "minecraft:ender_chest", states = listOf(CommonStates.minecraftCardinalDirection))
