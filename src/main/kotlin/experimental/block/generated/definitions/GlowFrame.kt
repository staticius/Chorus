package org.chorus_oss.chorus.experimental.block.generated.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.components.*
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object GlowFrame : BlockDefinition(
    identifier = "minecraft:glow_frame",
    states = listOf(CommonStates.facingDirection, CommonStates.itemFrameMapBit, CommonStates.itemFramePhotoBit),
    components = listOf(
        TransparentComponent(transparent = true),
        InternalFrictionComponent(internalFriction = 0.95f),
        MineableComponent(hardness = 0.25f),
        MoveableComponent(movement = MoveableComponent.Movement.Break, sticky = false),
        CollisionBoxComponent(enabled = false)
    )
)
