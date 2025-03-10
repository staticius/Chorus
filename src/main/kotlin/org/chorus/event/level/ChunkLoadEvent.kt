package org.chorus.event.level

import org.chorus.event.HandlerList
import org.chorus.level.format.IChunk


class ChunkLoadEvent(chunk: IChunk, val isNewChunk: Boolean) : ChunkEvent(chunk) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
