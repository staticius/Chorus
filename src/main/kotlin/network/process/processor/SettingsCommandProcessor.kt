package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.PlayerHandle
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.SettingsCommandPacket

class SettingsCommandProcessor : DataPacketProcessor<SettingsCommandPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: SettingsCommandPacket) {
        val player = playerHandle.player.asPlayer()

        val command = pk.command.lowercase()
        Server.instance.executeCommand(player, command)
    }

    override val packetId: Int
        get() = ProtocolInfo.SETTINGS_COMMAND_PACKET
}
