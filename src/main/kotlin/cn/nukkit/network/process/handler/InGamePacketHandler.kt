package cn.nukkit.network.process.handler

import cn.nukkit.network.connection.BedrockSession
import cn.nukkit.network.process.DataPacketManager
import cn.nukkit.network.protocol.DataPacket

class InGamePacketHandler(session: BedrockSession) : BedrockSessionPacketHandler(session) {
    val manager: DataPacketManager = DataPacketManager()

    fun managerHandle(pk: DataPacket) {
        if (manager.canProcess(pk.pid())) {
            manager.processPacket(handle!!, pk)
        }
    }
}
