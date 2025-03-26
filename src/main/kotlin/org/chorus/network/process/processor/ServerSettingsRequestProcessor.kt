package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.event.player.PlayerServerSettingsRequestEvent
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.network.protocol.ServerSettingsRequestPacket
import org.chorus.network.protocol.ServerSettingsResponsePacket

class ServerSettingsRequestProcessor : DataPacketProcessor<ServerSettingsRequestPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: ServerSettingsRequestPacket) {
        val settingsRequestEvent = PlayerServerSettingsRequestEvent(playerHandle.player, HashMap(playerHandle.serverSettings))
        Server.instance.pluginManager.callEvent(settingsRequestEvent)

        if (!settingsRequestEvent.isCancelled) {
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
