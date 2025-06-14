package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object BambooStandingSign :
    BlockDefinition(identifier = "minecraft:bamboo_standing_sign", states = listOf(CommonStates.groundSignDirection))
