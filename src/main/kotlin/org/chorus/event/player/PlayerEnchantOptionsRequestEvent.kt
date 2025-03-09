package org.chorus.event.player

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.inventory.EnchantInventory
import cn.nukkit.network.protocol.PlayerEnchantOptionsPacket.EnchantOptionData

class PlayerEnchantOptionsRequestEvent(player: Player?, table: EnchantInventory, options: List<EnchantOptionData>) :
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
