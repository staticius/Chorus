package org.chorus.event.block

import org.chorus.Player
import org.chorus.block.Block
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.utils.BlockColor

class SignColorChangeEvent(block: Block, val player: Player, val color: BlockColor) : BlockEvent(block),
    Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
