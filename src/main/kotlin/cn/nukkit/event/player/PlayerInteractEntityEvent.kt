package cn.nukkit.event.player

import cn.nukkit.Player
import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item
import cn.nukkit.math.Vector3

/**
 * @author CreeperFace
 * @since 1. 1. 2017
 */
class PlayerInteractEntityEvent(player: Player?, entity: Entity, item: Item, clickedPos: Vector3) :
    PlayerEvent(), Cancellable {
    val entity: Entity
    val item: Item
    val clickedPos: Vector3

    init {
        this.player = player
        this.entity = entity
        this.item = item
        this.clickedPos = clickedPos
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
