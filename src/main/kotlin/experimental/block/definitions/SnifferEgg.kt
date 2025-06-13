package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object SnifferEgg :
    BlockDefinition(
        identifier = "minecraft:sniffer_egg",
        states = listOf(CommonStates.crackedState)
    )
