package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object PaleHangingMoss :
    BlockDefinition(
        identifier = "minecraft:pale_hanging_moss",
        states = listOf(CommonStates.tip)
    )
