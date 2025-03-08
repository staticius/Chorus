package cn.nukkit.event.player

import cn.nukkit.AdventureSettings
import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

class PlayerGameModeChangeEvent(player: Player?, newGameMode: Int, newAdventureSettings: AdventureSettings) :
    PlayerEvent(), Cancellable {
    val newGamemode: Int
    @JvmField
    var newAdventureSettings: AdventureSettings

    init {
        this.player = player
        this.newGamemode = newGameMode
        this.newAdventureSettings = newAdventureSettings
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
