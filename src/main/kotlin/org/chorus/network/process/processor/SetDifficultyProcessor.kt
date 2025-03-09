package org.chorus.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.Server
import cn.nukkit.command.Command
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.lang.TranslationContainer
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.network.protocol.SetDifficultyPacket

class SetDifficultyProcessor : DataPacketProcessor<SetDifficultyPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: SetDifficultyPacket) {
        if (!playerHandle.player.spawned || !playerHandle.player.hasPermission("nukkit.command.difficulty")) {
            return
        }
        playerHandle.player.getServer().setDifficulty(pk.difficulty)
        val difficultyPacket = SetDifficultyPacket()
        difficultyPacket.difficulty = playerHandle.player.getServer().getDifficulty()
        Server.broadcastPacket(playerHandle.player.getServer().getOnlinePlayers().values, difficultyPacket)
        Command.broadcastCommandMessage(
            playerHandle.player,
            TranslationContainer(
                "commands.difficulty.success",
                playerHandle.player.getServer().getDifficulty().toString()
            )
        )
    }

    override val packetId: Int
        get() = ProtocolInfo.SET_DIFFICULTY_PACKET
}
