package org.chorus_oss.chorus.experimental.network.process.handler

import org.chorus_oss.chorus.network.connection.BedrockSession
import org.chorus_oss.chorus.network.process.SessionState
import org.chorus_oss.chorus.network.protocol.ClientToServerHandshakePacket

class HandshakeHandler(session: BedrockSession) : SessionHandler(session) {
    override fun handle(pk: ClientToServerHandshakePacket) {
        session.machine.fire(SessionState.RESOURCE_PACK)
    }
}
