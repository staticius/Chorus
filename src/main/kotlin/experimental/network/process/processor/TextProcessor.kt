package org.chorus_oss.chorus.experimental.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.TextPacket
import org.chorus_oss.chorus.utils.Loggable

class TextProcessor : DataPacketProcessor<TextPacket>() {
    override fun handle(player: Player, pk: TextPacket) {
        if (!player.player.spawned || !player.player.isAlive()) {
            return
        }

        val isXboxAuth = Server.instance.settings.serverSettings.xboxAuth
        if (isXboxAuth && pk.xboxUserId != player.player.loginChainData.xuid) {
            log.warn(
                "{} sent TextPacket with invalid xuid : {} != {}",
                player.player.getEntityName(),
                pk.xboxUserId,
                player.player.loginChainData.xuid
            )
            return
        }

        if (pk.parameters.size > 1) {
            player.player.close("§cPacket handling error")
            return
        }

        if (pk.type == TextPacket.TYPE_CHAT) {
            var chatMessage = pk.message
            val breakLine = chatMessage.indexOf('\n')
            // Chat messages shouldn't contain break lines so ignore text afterward
            if (breakLine != -1) {
                chatMessage = chatMessage.substring(0, breakLine)
            }
            player.player.chat(chatMessage)
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.TEXT_PACKET

    companion object : Loggable
}
