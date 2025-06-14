package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object DamagedAnvil :
    BlockDefinition(identifier = "minecraft:damaged_anvil", states = listOf(CommonStates.minecraftCardinalDirection))
