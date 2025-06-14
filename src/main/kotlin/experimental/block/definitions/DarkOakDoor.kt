package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object DarkOakDoor : BlockDefinition(
    identifier = "minecraft:dark_oak_door",
    states = listOf(
        CommonStates.doorHingeBit,
        CommonStates.minecraftCardinalDirection,
        CommonStates.openBit,
        CommonStates.upperBlockBit
    )
)
