package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object BrownMushroomBlock :
    BlockDefinition(
        identifier = "minecraft:brown_mushroom_block",
        states = listOf(CommonStates.hugeMushroomBits)
    )
