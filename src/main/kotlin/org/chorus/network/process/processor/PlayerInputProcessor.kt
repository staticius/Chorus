package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.entity.item.EntityMinecartAbstract
import org.chorus.entity.item.EntityMinecartAbstract.setCurrentSpeed
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.PlayerInputPacket
import org.chorus.network.protocol.ProtocolInfo

class PlayerInputProcessor : DataPacketProcessor<PlayerInputPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: PlayerInputPacket) {
        if (!playerHandle.player.isAlive() || !playerHandle.player.spawned) {
            return
        }
        if (playerHandle.player.riding is EntityMinecartAbstract) {
            riding.setCurrentSpeed(pk.motionY.toDouble())
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.PLAYER_INPUT_PACKET
}
