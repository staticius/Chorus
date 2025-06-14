package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object BambooSapling :
    BlockDefinition(identifier = "minecraft:bamboo_sapling", states = listOf(CommonStates.ageBit))
