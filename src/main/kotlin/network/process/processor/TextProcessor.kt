package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.PlayerHandle
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.TextPacket
import org.chorus_oss.chorus.utils.Loggable

class TextProcessor : DataPacketProcessor<TextPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: TextPacket) {
        if (!playerHandle.player.spawned || !playerHandle.player.isAlive()) {
            return
        }

        val isXboxAuth = Server.instance.settings.serverSettings.xboxAuth
        if (isXboxAuth && pk.xboxUserId != playerHandle.loginChainData.xuid) {
            TextProcessor.log.warn(
                "{} sent TextPacket with invalid xuid : {} != {}",
                playerHandle.username,
                pk.xboxUserId,
                playerHandle.loginChainData.xuid
            )
            return
        }

        if (pk.parameters.size > 1) {
            playerHandle.player.close("§cPacket handling error")
            return
        }

        if (pk.type == TextPacket.TYPE_CHAT) {
            var chatMessage = pk.message
            val breakLine = chatMessage.indexOf('\n')
            // Chat messages shouldn't contain break lines so ignore text afterward
            if (breakLine != -1) {
                chatMessage = chatMessage.substring(0, breakLine)
            }
            playerHandle.player.chat(chatMessage)
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.TEXT_PACKET

    companion object : Loggable
}
