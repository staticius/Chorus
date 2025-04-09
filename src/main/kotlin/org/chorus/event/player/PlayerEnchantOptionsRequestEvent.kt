package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.inventory.EnchantInventory
import org.chorus.network.protocol.PlayerEnchantOptionsPacket.EnchantOptionData

class PlayerEnchantOptionsRequestEvent(player: Player, table: EnchantInventory, options: List<EnchantOptionData>) :
    PlayerEvent(), Cancellable {
    val inventory: EnchantInventory

    @JvmField
    var options: List<EnchantOptionData>

    init {
        this.player = player
        this.inventory = table
        this.options = options
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
