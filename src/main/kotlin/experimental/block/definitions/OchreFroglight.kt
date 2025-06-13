package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object OchreFroglight :
    BlockDefinition(
        identifier = "minecraft:ochre_froglight",
        states = listOf(CommonStates.pillarAxis)
    )
