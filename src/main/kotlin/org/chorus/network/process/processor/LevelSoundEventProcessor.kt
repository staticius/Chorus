package org.chorus.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.LevelSoundEventPacket
import cn.nukkit.network.protocol.ProtocolInfo

open class LevelSoundEventProcessor : DataPacketProcessor<LevelSoundEventPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: LevelSoundEventPacket) {
        val player = playerHandle.player
        if (!player.isSpectator) {
            player.level!!.addChunkPacket(player.position.chunkX, player.position.chunkZ, pk)
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.LEVEL_SOUND_EVENT_PACKET
}
