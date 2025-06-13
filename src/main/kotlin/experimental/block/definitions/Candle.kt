package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Candle :
    BlockDefinition(
        identifier = "minecraft:candle",
        states = listOf(
            CommonStates.candles,
            CommonStates.lit
        )
    )
