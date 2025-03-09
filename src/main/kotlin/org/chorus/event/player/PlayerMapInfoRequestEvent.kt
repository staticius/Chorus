package org.chorus.event.player

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item

/**
 * @author CreeperFace
 * @since 18.3.2017
 */
class PlayerMapInfoRequestEvent(p: Player?, item: Item) : PlayerEvent(), Cancellable {
    val map: Item

    init {
        this.player = p
        this.map = item
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
