package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.entity.Entity.getServer
import org.chorus.event.player.PlayerDuplicatedLoginEvent
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.LoginPacket
import org.chorus.network.protocol.ProtocolInfo

class LoginProcessor : DataPacketProcessor<LoginPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: LoginPacket) {
        val player = playerHandle.player
        if (!player.session.isAuthenticated()) {
            return
        }

        val event = PlayerDuplicatedLoginEvent(player)
        player.getServer().getPluginManager().callEvent(event)

        if (event.isCancelled) {
            return
        }

        player.close("§cPacket handling error")
    }

    override val packetId: Int
        get() = ProtocolInfo.LOGIN_PACKET
}
