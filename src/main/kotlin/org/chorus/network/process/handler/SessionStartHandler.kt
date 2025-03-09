package org.chorus.network.process.handler

import cn.nukkit.Server
import cn.nukkit.network.connection.BedrockSession
import cn.nukkit.network.process.SessionState
import cn.nukkit.network.protocol.NetworkSettingsPacket
import cn.nukkit.network.protocol.PlayStatusPacket
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.network.protocol.RequestNetworkSettingsPacket
import cn.nukkit.network.protocol.types.PacketCompressionAlgorithm

class SessionStartHandler(session: BedrockSession) : BedrockSessionPacketHandler(session) {
    override fun handle(pk: RequestNetworkSettingsPacket) {
        val protocol = pk.protocolVersion
        if (protocol != ProtocolInfo.CURRENT_PROTOCOL) {
            session.sendPlayStatus(
                if (protocol < ProtocolInfo.CURRENT_PROTOCOL) PlayStatusPacket.LOGIN_FAILED_CLIENT else PlayStatusPacket.LOGIN_FAILED_SERVER,
                true
            )
            val message =
                if (protocol < ProtocolInfo.CURRENT_PROTOCOL) "disconnectionScreen.outdatedClient" else "disconnectionScreen.outdatedServer"
            session.close(message)
            return
        }

        val server = session.server
        if (server.ipBans.isBanned(session.addressString)) {
            val reason = server.ipBans.entires[session.addressString]!!.reason
            session.close(if (!reason.isEmpty()) "You are banned. Reason: $reason" else "You are banned")
            return
        }

        val settingsPacket = NetworkSettingsPacket()
        //FIXME there is no way out there to disable compression
        val algorithm = if (Server.getInstance().settings.networkSettings().snappy()) {
            PacketCompressionAlgorithm.SNAPPY
        } else {
            PacketCompressionAlgorithm.ZLIB
        }
        settingsPacket.compressionAlgorithm = algorithm
        settingsPacket.compressionThreshold = 1 // compress everything
        //In raknet version 11, the client does not enable packet compression by default,but the server will tell client what the
        //compression algorithm through NetworkSettingsPacket
        session.sendNetworkSettingsPacket(settingsPacket)
        session.setCompression(algorithm) //so send the NetworkSettingsPacket packet before set the session compression
        session.machine.fire(SessionState.LOGIN)
    }
}
