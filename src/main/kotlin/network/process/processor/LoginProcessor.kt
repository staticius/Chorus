package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.PlayerHandle
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.event.player.PlayerDuplicatedLoginEvent
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.LoginPacket
import org.chorus_oss.chorus.network.protocol.ProtocolInfo

class LoginProcessor : DataPacketProcessor<LoginPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: LoginPacket) {
        val player = playerHandle.player
        if (!player.session.authenticated) {
            return
        }

        val event = PlayerDuplicatedLoginEvent(player)
        Server.instance.pluginManager.callEvent(event)

        if (event.cancelled) {
            return
        }

        player.close("§cPacket handling error")
    }

    override val packetId: Int
        get() = ProtocolInfo.LOGIN_PACKET
}
