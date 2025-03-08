package cn.nukkit.event.level

import cn.nukkit.event.HandlerList
import cn.nukkit.level.format.IChunk

/**
 * @author MagicDroidX (Nukkit Project)
 */
class ChunkLoadEvent(chunk: IChunk, val isNewChunk: Boolean) : ChunkEvent(chunk) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
