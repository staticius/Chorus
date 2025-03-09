package org.chorus.event.level

import org.chorus.event.HandlerList
import org.chorus.level.format.IChunk

/**
 * @author MagicDroidX (Nukkit Project)
 */
class ChunkLoadEvent(chunk: IChunk, val isNewChunk: Boolean) : ChunkEvent(chunk) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
