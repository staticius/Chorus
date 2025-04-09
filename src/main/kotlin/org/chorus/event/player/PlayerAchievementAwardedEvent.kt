package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class PlayerAchievementAwardedEvent(player: Player, achievementId: String) : PlayerEvent(), Cancellable {
    val achievement: String

    init {
        this.player = player
        this.achievement = achievementId
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
