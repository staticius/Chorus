package org.chorus.event.player

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

class PlayerAchievementAwardedEvent(player: Player?, achievementId: String) : PlayerEvent(), Cancellable {
    val achievement: String

    init {
        this.player = player
        this.achievement = achievementId
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
