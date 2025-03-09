package org.chorus.network.process.handler

import org.chorus.network.connection.BedrockSession
import org.chorus.network.process.DataPacketManager
import org.chorus.network.protocol.DataPacket

class InGamePacketHandler(session: BedrockSession) : BedrockSessionPacketHandler(session) {
    val manager: DataPacketManager = DataPacketManager()

    fun managerHandle(pk: DataPacket) {
        if (manager.canProcess(pk.pid())) {
            manager.processPacket(handle!!, pk)
        }
    }
}
