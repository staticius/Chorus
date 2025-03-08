package cn.nukkit.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.Server
import cn.nukkit.config.ServerPropertiesKeys
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.network.protocol.TextPacket
import lombok.extern.slf4j.Slf4j

@Slf4j
class TextProcessor : DataPacketProcessor<TextPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: TextPacket) {
        if (!playerHandle.player.spawned || !playerHandle.player.isAlive()) {
            return
        }

        val isXboxAuth = Server.getInstance().properties.get(ServerPropertiesKeys.XBOX_AUTH, true)
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
            // Chat messages shouldn't contain break lines so ignore text afterwards
            if (breakLine != -1) {
                chatMessage = chatMessage.substring(0, breakLine)
            }
            playerHandle.player.chat(chatMessage)
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.TEXT_PACKET
}
