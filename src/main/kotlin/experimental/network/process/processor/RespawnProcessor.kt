package org.chorus_oss.chorus.experimental.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.RespawnPacket

class RespawnProcessor : DataPacketProcessor<RespawnPacket>() {
    override fun handle(player: Player, pk: RespawnPacket) {
        val player = player.player
        if (player.isAlive()) {
            return
        }
        if (pk.respawnState == RespawnPacket.STATE_CLIENT_READY_TO_SPAWN) {
            val respawn1 = RespawnPacket()
            respawn1.x = player.getX().toFloat()
            respawn1.y = player.getY().toFloat()
            respawn1.z = player.getZ().toFloat()
            respawn1.respawnState = RespawnPacket.STATE_READY_TO_SPAWN
            player.dataPacket(respawn1)
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.RESPAWN_PACKET
}
