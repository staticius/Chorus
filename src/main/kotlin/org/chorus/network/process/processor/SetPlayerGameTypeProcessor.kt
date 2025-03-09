package org.chorus.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.Server
import cn.nukkit.command.Command
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.lang.TranslationContainer
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.network.protocol.SetPlayerGameTypePacket

class SetPlayerGameTypeProcessor : DataPacketProcessor<SetPlayerGameTypePacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: SetPlayerGameTypePacket) {
        if (pk.gamemode != playerHandle.player.gamemode && playerHandle.player.hasPermission("nukkit.command.gamemode")) {
            playerHandle.player.setGamemode(
                when (pk.gamemode) {
                    0, 1, 2 -> pk.gamemode
                    6 -> 3
                    5 -> playerHandle.player.getServer().getDefaultGamemode()
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
