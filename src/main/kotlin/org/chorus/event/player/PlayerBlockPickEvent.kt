package org.chorus.event.player

import cn.nukkit.Player
import cn.nukkit.block.Block
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item

/**
 * @author CreeperFace
 */
class PlayerBlockPickEvent(player: Player?, val blockClicked: Block, @JvmField var item: Item) :
    PlayerEvent(), Cancellable {
    init {
        this.player = player
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
