package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object RespawnAnchor :
    BlockDefinition(identifier = "minecraft:respawn_anchor", states = listOf(CommonStates.respawnAnchorCharge))
