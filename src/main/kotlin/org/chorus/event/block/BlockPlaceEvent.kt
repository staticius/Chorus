package org.chorus.event.block

import org.chorus.Player
import org.chorus.block.Block
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item

/**
 * @author MagicDroidX (Nukkit Project)
 */
class BlockPlaceEvent(
    val player: Player,
    blockPlace: Block,
    val blockReplace: Block,
    val blockAgainst: Block,
    val item: Item
) :
    BlockEvent(blockPlace), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
