package org.chorus_oss.chorus.event.player

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.event.Cancellable

class PlayerDuplicatedLoginEvent(player: Player) : PlayerEvent(), Cancellable {
    init {
        this.player = player
    }
}
