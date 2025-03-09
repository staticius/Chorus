package org.chorus.event.block

import cn.nukkit.Player
import cn.nukkit.block.Block
import cn.nukkit.event.HandlerList

/**
 * @author Snake1999
 * @since 2016/1/22
 */
class DoorToggleEvent(block: Block, @JvmField var player: Player) : BlockUpdateEvent(block) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
