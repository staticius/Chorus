package org.chorus.event.player

import cn.nukkit.Player
import cn.nukkit.event.Cancellable

class PlayerDuplicatedLoginEvent(player: Player?) : PlayerEvent(), Cancellable {
    init {
        this.player = player
    }
}
