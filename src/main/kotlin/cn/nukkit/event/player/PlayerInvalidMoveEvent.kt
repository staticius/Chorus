package cn.nukkit.event.player

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

/**
 * call when a player moves wrongly
 *
 * @author WilliamGao
 */
class PlayerInvalidMoveEvent(player: Player?, revert: Boolean) : PlayerEvent(), Cancellable {
    val isRevert: Boolean

    init {
        this.player = player
        this.isRevert = revert
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
