package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object Vault : BlockDefinition(
    identifier = "minecraft:vault",
    states = listOf(CommonStates.minecraftCardinalDirection, CommonStates.ominous, CommonStates.vaultState)
)
