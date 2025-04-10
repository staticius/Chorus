package org.chorus.event.block

import org.chorus.block.Block
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.level.Locator

/**
 * @author joserobjr
 * @since 2020-10-06
 */
class BlockExplodeEvent(
    block: Block,
    val position: Locator,
    var affectedBlocks: Set<Block>,
    @JvmField var ignitions: Set<Block>,
    @JvmField var yield: Double,
    val fireChance: Double
) :
    BlockEvent(block), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
