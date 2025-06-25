package org.chorus_oss.chorus.experimental.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.SettingsCommandPacket

class SettingsCommandProcessor : DataPacketProcessor<SettingsCommandPacket>() {
    override fun handle(player: Player, pk: SettingsCommandPacket) {
        val player = player.player.asPlayer()

        val command = pk.command.lowercase()
        Server.instance.executeCommand(player, command)
    }

    override val packetId: Int
        get() = ProtocolInfo.SETTINGS_COMMAND_PACKET
}
