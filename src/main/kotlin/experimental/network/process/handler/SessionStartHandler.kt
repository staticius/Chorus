package org.chorus_oss.chorus.experimental.network.process.handler

import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.network.connection.BedrockSession
import org.chorus_oss.chorus.network.process.SessionState
import org.chorus_oss.chorus.network.protocol.NetworkSettingsPacket
import org.chorus_oss.chorus.network.protocol.PlayStatusPacket
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.RequestNetworkSettingsPacket
import org.chorus_oss.chorus.network.protocol.types.PacketCompressionAlgorithm

class SessionStartHandler(session: BedrockSession) : SessionHandler(session) {
    override fun handle(pk: RequestNetworkSettingsPacket) {
        val protocol = pk.protocolVersion
        if (protocol != ProtocolInfo.PROTOCOL_VERSION) {
            session.sendPlayStatus(
                if (protocol < ProtocolInfo.PROTOCOL_VERSION) PlayStatusPacket.LOGIN_FAILED_CLIENT else PlayStatusPacket.LOGIN_FAILED_SERVER,
                true
            )
            val message =
                if (protocol < ProtocolInfo.PROTOCOL_VERSION) "disconnectionScreen.outdatedClient" else "disconnectionScreen.outdatedServer"
            session.close(message)
            return
        }

        val server = session.server
        if (server.bannedIPs.isBanned(session.addressString)) {
            val reason = server.bannedIPs.entries[session.addressString]!!.reason
            session.close(if (reason.isNotEmpty()) "You are banned. Reason: $reason" else "You are banned")
            return
        }

        val settingsPacket = NetworkSettingsPacket()
        //FIXME there is no way out there to disable compression
        val algorithm = if (Server.instance.settings.networkSettings.snappy) {
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
