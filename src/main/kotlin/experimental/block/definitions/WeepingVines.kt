package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object WeepingVines :
    BlockDefinition(
        identifier = "minecraft:weeping_vines",
        states = listOf(CommonStates.weepingVinesAge)
    )
