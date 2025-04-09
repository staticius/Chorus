package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable

class PlayerDuplicatedLoginEvent(player: Player) : PlayerEvent(), Cancellable {
    init {
        this.player = player
    }
}
