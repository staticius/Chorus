package org.chorus_oss.chorus.event.player

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.event.Cancellable
import org.chorus_oss.chorus.event.HandlerList
import org.chorus_oss.chorus.inventory.EnchantInventory
import org.chorus_oss.chorus.network.protocol.PlayerEnchantOptionsPacket.EnchantOptionData

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
