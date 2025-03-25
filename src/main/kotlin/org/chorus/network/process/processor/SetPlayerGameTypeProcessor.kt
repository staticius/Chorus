package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.command.Command
import org.chorus.lang.TranslationContainer
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.network.protocol.SetPlayerGameTypePacket

class SetPlayerGameTypeProcessor : DataPacketProcessor<SetPlayerGameTypePacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: SetPlayerGameTypePacket) {
        if (pk.gamemode != playerHandle.player.gamemode && playerHandle.player.hasPermission("chorus.command.gamemode")) {
            playerHandle.player.setGamemode(
                when (pk.gamemode) {
                    0, 1, 2 -> pk.gamemode
                    6 -> 3
                    5 -> Server.instance.getDefaultGamemode()
                    else -> throw IllegalStateException("Unexpected value: " + pk.gamemode)
                }
            )
            Command.broadcastCommandMessage(
                playerHandle.player,
                TranslationContainer(
                    "commands.gamemode.success.self",
                    Server.getGamemodeString(playerHandle.player.gamemode)
                )
            )
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET
}
