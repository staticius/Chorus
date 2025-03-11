package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.command.Command
import org.chorus.lang.TranslationContainer
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.network.protocol.SetDifficultyPacket

class SetDifficultyProcessor : DataPacketProcessor<SetDifficultyPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: SetDifficultyPacket) {
        if (!playerHandle.player.spawned || !playerHandle.player.hasPermission("nukkit.command.difficulty")) {
            return
        }
        playerHandle.Server.instance.setDifficulty(pk.difficulty)
        val difficultyPacket = SetDifficultyPacket()
        difficultyPacket.difficulty = playerHandle.Server.instance.getDifficulty()
        Server.broadcastPacket(playerHandle.Server.instance.getOnlinePlayers().values, difficultyPacket)
        Command.broadcastCommandMessage(
            playerHandle.player,
            TranslationContainer(
                "commands.difficulty.success",
                playerHandle.Server.instance.getDifficulty().toString()
            )
        )
    }

    override val packetId: Int
        get() = ProtocolInfo.SET_DIFFICULTY_PACKET
}
