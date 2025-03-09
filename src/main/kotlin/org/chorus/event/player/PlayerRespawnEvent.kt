package org.chorus.event.player

import cn.nukkit.Player
import cn.nukkit.event.HandlerList
import cn.nukkit.level.Locator
import cn.nukkit.network.protocol.types.SpawnPointType
import it.unimi.dsi.fastutil.Pair

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
