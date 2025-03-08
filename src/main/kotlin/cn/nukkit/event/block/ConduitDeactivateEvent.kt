package cn.nukkit.event.block

import cn.nukkit.block.Block
import cn.nukkit.event.HandlerList

class ConduitDeactivateEvent(block: Block) : BlockEvent(block) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
