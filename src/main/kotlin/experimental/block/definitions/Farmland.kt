package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Farmland :
    BlockDefinition(identifier = "minecraft:farmland", states = listOf(CommonStates.moisturizedAmount))
