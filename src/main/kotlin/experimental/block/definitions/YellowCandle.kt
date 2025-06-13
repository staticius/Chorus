package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object YellowCandle :
    BlockDefinition(
        identifier = "minecraft:yellow_candle",
        states = listOf(
            CommonStates.candles,
            CommonStates.lit
        )
    )
