package org.chorus.event.block

import org.chorus.Player
import org.chorus.block.Block
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class SignGlowEvent(block: Block, val player: Player, val isGlowing: Boolean) : BlockEvent(block),
    Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}