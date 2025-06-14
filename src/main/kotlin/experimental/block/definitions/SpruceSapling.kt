package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SpruceSapling :
    BlockDefinition(identifier = "minecraft:spruce_sapling", states = listOf(CommonStates.ageBit))
