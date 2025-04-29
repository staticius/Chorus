package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.PlayerHandle
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.command.Command
import org.chorus_oss.chorus.lang.TranslationContainer
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.SetDifficultyPacket

class SetDifficultyProcessor : DataPacketProcessor<SetDifficultyPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: SetDifficultyPacket) {
        if (!playerHandle.player.spawned || !playerHandle.player.hasPermission("chorus.command.difficulty")) {
            return
        }
        Server.instance.setDifficulty(pk.difficulty)
        val difficultyPacket = SetDifficultyPacket()
        difficultyPacket.difficulty = Server.instance.getDifficulty()
        Server.broadcastPacket(Server.instance.onlinePlayers.values, difficultyPacket)
        Command.broadcastCommandMessage(
            playerHandle.player,
            TranslationContainer(
                "commands.difficulty.success",
                Server.instance.getDifficulty().toString()
            )
        )
    }

    override val packetId: Int
        get() = ProtocolInfo.SET_DIFFICULTY_PACKET
}
