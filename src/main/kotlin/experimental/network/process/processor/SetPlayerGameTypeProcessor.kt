package org.chorus_oss.chorus.experimental.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.command.Command
import org.chorus_oss.chorus.lang.TranslationContainer
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.SetPlayerGameTypePacket

class SetPlayerGameTypeProcessor : DataPacketProcessor<SetPlayerGameTypePacket>() {
    override fun handle(player: Player, pk: SetPlayerGameTypePacket) {
        if (pk.gamemode != player.player.gamemode && player.player.hasPermission("chorus.command.gamemode")) {
            player.player.setGamemode(
                when (pk.gamemode) {
                    0, 1, 2 -> pk.gamemode
                    6 -> 3
                    5 -> Server.instance.defaultGamemode
                    else -> throw IllegalStateException("Unexpected value: " + pk.gamemode)
                }
            )
            Command.broadcastCommandMessage(
                player.player,
                TranslationContainer(
                    "commands.gamemode.success.self",
                    Server.getGamemodeString(player.player.gamemode)
                )
            )
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET
}
