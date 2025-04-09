package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class PlayerToggleSpinAttackEvent(player: Player, isSpinAttacking: Boolean) : PlayerEvent(), Cancellable {
    val isSpinAttacking: Boolean

    init {
        this.player = player
        this.isSpinAttacking = isSpinAttacking
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
