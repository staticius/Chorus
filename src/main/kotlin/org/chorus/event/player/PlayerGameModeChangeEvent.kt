package org.chorus.event.player

import org.chorus.AdventureSettings
import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

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
