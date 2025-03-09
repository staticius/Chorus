package org.chorus.event.block

import org.chorus.Player
import org.chorus.block.Block
import org.chorus.event.HandlerList

/**
 * @author Snake1999
 * @since 2016/1/22
 */
class DoorToggleEvent(block: Block, @JvmField var player: Player) : BlockUpdateEvent(block) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
