package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.network.protocol.SettingsCommandPacket

class SettingsCommandProcessor : DataPacketProcessor<SettingsCommandPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: SettingsCommandPacket) {
        val player = playerHandle.player.asPlayer()

        val command = pk.command.lowercase()
        Server.instance.executeCommand(player, command)
    }

    override val packetId: Int
        get() = ProtocolInfo.SETTINGS_COMMAND_PACKET
}
