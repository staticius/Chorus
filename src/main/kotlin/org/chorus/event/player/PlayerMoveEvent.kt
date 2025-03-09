package org.chorus.event.player

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.level.Transform

class PlayerMoveEvent @JvmOverloads constructor(
    player: Player?,
    from: Transform,
    to: Transform,
    resetBlocks: Boolean = true
) :
    PlayerEvent(), Cancellable {
    var from: Transform
    @JvmField
    var to: Transform

    var isResetBlocksAround: Boolean

    init {
        this.player = player
        this.from = from
        this.to = to
        this.isResetBlocksAround = resetBlocks
    }

    override fun setCancelled() {
        super.setCancelled()
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
