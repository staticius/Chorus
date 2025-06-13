package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object TwistingVines :
    BlockDefinition(
        identifier = "minecraft:twisting_vines",
        states = listOf(CommonStates.twistingVinesAge)
    )
