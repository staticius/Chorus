package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.PlayerHandle
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.LevelSoundEventPacket
import org.chorus_oss.chorus.network.protocol.ProtocolInfo

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
