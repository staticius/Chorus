package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object GrayCandleCake :
    BlockDefinition(
        identifier = "minecraft:gray_candle_cake",
        states = listOf(CommonStates.lit)
    )
