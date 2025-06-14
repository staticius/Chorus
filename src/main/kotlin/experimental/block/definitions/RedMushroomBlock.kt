package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object RedMushroomBlock :
    BlockDefinition(identifier = "minecraft:red_mushroom_block", states = listOf(CommonStates.hugeMushroomBits))
