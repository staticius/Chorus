package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.network.protocol.AnimatePacket

class PlayerAnimationEvent : PlayerEvent, Cancellable {
    @JvmField
    val animationType: AnimatePacket.Action

    @JvmField
    val rowingTime: Float?

    constructor(player: Player, animatePacket: AnimatePacket) {
        this.player = player
        animationType = animatePacket.action
        rowingTime = when (animationType) {
            AnimatePacket.Action.ROW_LEFT,
            AnimatePacket.Action.ROW_RIGHT -> {
                val actionData = animatePacket.actionData as AnimatePacket.Action.RowingData
                actionData.rowingTime
            }

            else -> null
        }
    }

    @JvmOverloads
    constructor(player: Player, animation: AnimatePacket.Action = AnimatePacket.Action.SWING_ARM) {
        this.player = player
        this.animationType = animation
        rowingTime = 0f
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
