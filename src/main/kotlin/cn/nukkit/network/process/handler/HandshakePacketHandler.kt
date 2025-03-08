package cn.nukkit.network.process.handler

import cn.nukkit.network.connection.BedrockSession
import cn.nukkit.network.process.SessionState
import cn.nukkit.network.protocol.ClientToServerHandshakePacket

class HandshakePacketHandler(session: BedrockSession) : BedrockSessionPacketHandler(session) {
    override fun handle(pk: ClientToServerHandshakePacket) {
        session.machine.fire(SessionState.RESOURCE_PACK)
    }
}
