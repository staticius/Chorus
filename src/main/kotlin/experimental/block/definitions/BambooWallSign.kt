package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object BambooWallSign :
    BlockDefinition(identifier = "minecraft:bamboo_wall_sign", states = listOf(CommonStates.facingDirection))
