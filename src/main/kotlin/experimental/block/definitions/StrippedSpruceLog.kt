package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object StrippedSpruceLog :
    BlockDefinition(identifier = "minecraft:stripped_spruce_log", states = listOf(CommonStates.pillarAxis))
