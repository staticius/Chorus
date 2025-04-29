package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.PlayerHandle
import org.chorus_oss.chorus.entity.item.EntityMinecartAbstract
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.PlayerInputPacket
import org.chorus_oss.chorus.network.protocol.ProtocolInfo

class PlayerInputProcessor : DataPacketProcessor<PlayerInputPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: PlayerInputPacket) {
        if (!playerHandle.player.isAlive() || !playerHandle.player.spawned) {
            return
        }
        val riding = playerHandle.player.riding
        if (riding is EntityMinecartAbstract) {
            riding.setCurrentSpeed(pk.motionY.toDouble())
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.PLAYER_INPUT_PACKET
}
