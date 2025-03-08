package cn.nukkit.event.player

import cn.nukkit.Player
import cn.nukkit.event.HandlerList

class PlayerChunkRequestEvent(player: Player?, chunkX: Int, chunkZ: Int) : PlayerEvent() {
    val chunkX: Int
    val chunkZ: Int

    init {
        this.player = player
        this.chunkX = chunkX
        this.chunkZ = chunkZ
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
