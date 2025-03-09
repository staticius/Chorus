package org.chorus.network.process.handler

import org.chorus.network.connection.BedrockSession
import org.chorus.network.process.SessionState
import org.chorus.network.protocol.ClientToServerHandshakePacket

class HandshakePacketHandler(session: BedrockSession) : BedrockSessionPacketHandler(session) {
    override fun handle(pk: ClientToServerHandshakePacket) {
        session.machine.fire(SessionState.RESOURCE_PACK)
    }
}
