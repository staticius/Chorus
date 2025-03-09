package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class PlayerFoodLevelChangeEvent(player: Player?, foodLevel: Int, foodSaturationLevel: Float) :
    PlayerEvent(), Cancellable {
    @JvmField
    var foodLevel: Int
    @JvmField
    var foodSaturationLevel: Float

    init {
        this.player = player
        this.foodLevel = foodLevel
        this.foodSaturationLevel = foodSaturationLevel
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
