package org.chorus.event.player

import it.unimi.dsi.fastutil.Pair
import org.chorus.Player
import org.chorus.event.HandlerList
import org.chorus.level.Locator
import org.chorus.network.protocol.types.SpawnPointType

class PlayerRespawnEvent(player: Player?, position: Pair<Locator, SpawnPointType>) :
    PlayerEvent() {
    var respawnPosition: Pair<Locator, SpawnPointType> //Respawn Position

    init {
        this.player = player
        this.respawnPosition = position
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
