package org.chorus.event.block

import cn.nukkit.block.Block
import cn.nukkit.event.HandlerList

class ConduitActivateEvent(block: Block) : BlockEvent(block) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
