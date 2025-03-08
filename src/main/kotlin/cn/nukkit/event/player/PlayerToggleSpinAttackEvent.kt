package cn.nukkit.event.player

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

/**
 * @author GoodLucky777
 */
class PlayerToggleSpinAttackEvent(player: Player?, isSpinAttacking: Boolean) : PlayerEvent(), Cancellable {
    val isSpinAttacking: Boolean

    init {
        this.player = player
        this.isSpinAttacking = isSpinAttacking
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
