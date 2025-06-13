package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object LimeCandleCake :
    BlockDefinition(
        identifier = "minecraft:lime_candle_cake",
        states = listOf(CommonStates.lit)
    )
