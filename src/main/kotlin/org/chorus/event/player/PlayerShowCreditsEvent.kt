package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

/**
 * @author GoodLucky777
 */
class PlayerShowCreditsEvent(player: Player?) : PlayerEvent(), Cancellable {
    init {
        this.player = player
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
