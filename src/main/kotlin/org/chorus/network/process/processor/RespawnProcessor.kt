package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.entity.Entity.getX
import org.chorus.entity.Entity.getY
import org.chorus.entity.Entity.getZ
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.network.protocol.RespawnPacket

class RespawnProcessor : DataPacketProcessor<RespawnPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: RespawnPacket) {
        val player = playerHandle.player
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
