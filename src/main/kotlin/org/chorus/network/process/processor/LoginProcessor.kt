package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.event.player.PlayerDuplicatedLoginEvent
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.LoginPacket
import org.chorus.network.protocol.ProtocolInfo

class LoginProcessor : DataPacketProcessor<LoginPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: LoginPacket) {
        val player = playerHandle.player
        if (!player.session.authenticated) {
            return
        }

        val event = PlayerDuplicatedLoginEvent(player)
        Server.instance.pluginManager.callEvent(event)

        if (event.isCancelled) {
            return
        }

        player.close("§cPacket handling error")
    }

    override val packetId: Int
        get() = ProtocolInfo.LOGIN_PACKET
}
