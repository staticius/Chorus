package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object RedstoneWire :
    BlockDefinition(
        identifier = "minecraft:redstone_wire",
        states = listOf(CommonStates.redstoneSignal)
    )
