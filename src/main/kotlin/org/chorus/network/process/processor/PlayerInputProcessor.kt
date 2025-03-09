package org.chorus.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.entity.item.EntityMinecartAbstract
import cn.nukkit.entity.item.EntityMinecartAbstract.setCurrentSpeed
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.PlayerInputPacket
import cn.nukkit.network.protocol.ProtocolInfo

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
