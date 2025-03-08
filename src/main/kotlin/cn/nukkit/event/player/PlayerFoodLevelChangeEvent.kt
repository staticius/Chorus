package cn.nukkit.event.player

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

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
