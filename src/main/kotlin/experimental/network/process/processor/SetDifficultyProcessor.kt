package org.chorus_oss.chorus.experimental.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.command.Command
import org.chorus_oss.chorus.lang.TranslationContainer
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.SetDifficultyPacket

class SetDifficultyProcessor : DataPacketProcessor<SetDifficultyPacket>() {
    override fun handle(player: Player, pk: SetDifficultyPacket) {
        if (!player.player.spawned || !player.player.hasPermission("chorus.command.difficulty")) {
            return
        }
        Server.instance.setDifficulty(pk.difficulty)
        val difficultyPacket = SetDifficultyPacket()
        difficultyPacket.difficulty = Server.instance.getDifficulty()
        Server.broadcastPacket(Server.instance.onlinePlayers.values, difficultyPacket)
        Command.broadcastCommandMessage(
            player.player,
            TranslationContainer(
                "commands.difficulty.success",
                Server.instance.getDifficulty().toString()
            )
        )
    }

    override val packetId: Int
        get() = ProtocolInfo.SET_DIFFICULTY_PACKET
}
