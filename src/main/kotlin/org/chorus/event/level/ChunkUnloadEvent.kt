package org.chorus.event.level

import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.level.format.IChunk

/**
 * @author MagicDroidX (Nukkit Project)
 */
class ChunkUnloadEvent(chunk: IChunk) : ChunkEvent(chunk), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
