package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

/**
 * call when a player moves wrongly
 *
 * @author WilliamGao
 */
class PlayerInvalidMoveEvent(player: Player?, revert: Boolean) : PlayerEvent(), Cancellable {
    val isRevert: Boolean

    init {
        this.player = player
        this.isRevert = revert
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
