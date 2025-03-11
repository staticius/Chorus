package org.chorus.event.level

import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.level.format.IChunk


class ChunkUnloadEvent(chunk: IChunk) : ChunkEvent(chunk), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
