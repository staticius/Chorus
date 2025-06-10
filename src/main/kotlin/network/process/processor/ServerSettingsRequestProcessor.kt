package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.PlayerHandle
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.event.player.PlayerServerSettingsRequestEvent
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.ServerSettingsRequestPacket
import org.chorus_oss.chorus.network.protocol.ServerSettingsResponsePacket

class ServerSettingsRequestProcessor : DataPacketProcessor<ServerSettingsRequestPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: ServerSettingsRequestPacket) {
        val settingsRequestEvent =
            PlayerServerSettingsRequestEvent(playerHandle.player, HashMap(playerHandle.serverSettings))
        Server.instance.pluginManager.callEvent(settingsRequestEvent)

        if (!settingsRequestEvent.cancelled) {
            settingsRequestEvent.getSettings().forEach { (id, window) ->
                val re = ServerSettingsResponsePacket()
                re.formId = id
                re.data = window.toJson()
                playerHandle.player.dataPacket(re)
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.SERVER_SETTINGS_REQUEST_PACKET
}
