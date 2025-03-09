package org.chorus.event.player

import cn.nukkit.Player
import cn.nukkit.event.HandlerList
import cn.nukkit.lang.TextContainer

/**
 * Called when the player spawns in the world after logging in, when they first see the terrain.
 *
 *
 * Note: A lot of data is sent to the player between login and this event. Disconnecting the player during this event
 * will cause this data to be wasted. Prefer disconnecting at login-time if possible to minimize bandwidth wastage.
 *
 * @see PlayerLoginEvent
 */
class PlayerJoinEvent : PlayerEvent {
    @JvmField
    var joinMessage: TextContainer

    constructor(player: Player?, joinMessage: TextContainer) {
        this.player = player
        this.joinMessage = joinMessage
    }

    constructor(player: Player?, joinMessage: String?) {
        this.player = player
        this.joinMessage = TextContainer(joinMessage)
    }

    fun setJoinMessage(joinMessage: String?) {
        this.joinMessage = TextContainer(joinMessage)
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
