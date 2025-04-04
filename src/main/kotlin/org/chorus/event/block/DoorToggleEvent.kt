package org.chorus.event.block

import org.chorus.Player
import org.chorus.block.Block
import org.chorus.event.HandlerList

class DoorToggleEvent(block: Block, @JvmField var player: Player?) : BlockUpdateEvent(block) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
