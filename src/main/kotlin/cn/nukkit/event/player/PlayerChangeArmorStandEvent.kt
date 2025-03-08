package cn.nukkit.event.player

import cn.nukkit.Player
import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item

class PlayerChangeArmorStandEvent(player: Player?, armorStand: Entity, item: Item, slot: Int) :
    PlayerEvent(), Cancellable {
    val armorStand: Entity
    val slot: Int
    var item: Item

    init {
        this.player = player
        this.armorStand = armorStand
        this.item = item
        this.slot = slot
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
