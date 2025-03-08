package cn.nukkit.event.player

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.network.protocol.AnimatePacket

class PlayerAnimationEvent : PlayerEvent, Cancellable {
    @JvmField
    val animationType: AnimatePacket.Action
    @JvmField
    val rowingTime: Float

    constructor(player: Player?, animatePacket: AnimatePacket) {
        this.player = player
        animationType = animatePacket.action
        rowingTime = animatePacket.rowingTime
    }

    @JvmOverloads
    constructor(player: Player?, animation: AnimatePacket.Action = AnimatePacket.Action.SWING_ARM) {
        this.player = player
        this.animationType = animation
        rowingTime = 0f
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
