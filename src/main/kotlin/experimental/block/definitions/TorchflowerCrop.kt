package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object TorchflowerCrop :
    BlockDefinition(
        identifier = "minecraft:torchflower_crop",
        states = listOf(CommonStates.growth)
    )
