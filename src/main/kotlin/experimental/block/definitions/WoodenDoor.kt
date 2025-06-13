package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object WoodenDoor : BlockDefinition(
    identifier = "minecraft:wooden_door",
    states = listOf(
        CommonStates.minecraftCardinalDirection,
        CommonStates.openBit,
        CommonStates.upperBlockBit,
        CommonStates.doorHingeBit
    )
)
