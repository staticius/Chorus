package org.chorus.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.network.protocol.SettingsCommandPacket

class SettingsCommandProcessor : DataPacketProcessor<SettingsCommandPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: SettingsCommandPacket) {
        val player = playerHandle.player.asPlayer()

        val command = pk.command.lowercase()
        player.getServer().executeCommand(player, command)
    }

    override val packetId: Int
        get() = ProtocolInfo.SETTINGS_COMMAND_PACKET
}
