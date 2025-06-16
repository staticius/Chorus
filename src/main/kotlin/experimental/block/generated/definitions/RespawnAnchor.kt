package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.MapColorComponent
import org.chorus_oss.chorus.experimental.block.components.MineableComponent
import org.chorus_oss.chorus.experimental.block.components.MoveableComponent
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object RespawnAnchor : BlockDefinition(
    identifier = "minecraft:respawn_anchor",
    states = listOf(CommonStates.respawnAnchorCharge),
    components = listOf(
        MapColorComponent(r = 25, g = 25, b = 25, a = 255),
        MineableComponent(hardness = 50.0f),
        MoveableComponent(movement = MoveableComponent.Movement.None, sticky = false)
    )
)
