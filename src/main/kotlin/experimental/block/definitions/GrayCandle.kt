package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object GrayCandle :
    BlockDefinition(identifier = "minecraft:gray_candle", states = listOf(CommonStates.candles, CommonStates.lit))
