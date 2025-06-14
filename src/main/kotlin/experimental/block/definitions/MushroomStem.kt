package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object MushroomStem :
    BlockDefinition(identifier = "minecraft:mushroom_stem", states = listOf(CommonStates.hugeMushroomBits))
