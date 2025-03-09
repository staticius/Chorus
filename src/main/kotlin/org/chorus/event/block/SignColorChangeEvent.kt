package org.chorus.event.block

import cn.nukkit.Player
import cn.nukkit.block.Block
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.utils.BlockColor

class SignColorChangeEvent(block: Block, val player: Player, val color: BlockColor) : BlockEvent(block),
    Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
