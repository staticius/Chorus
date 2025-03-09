package org.chorus.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.event.player.PlayerServerSettingsRequestEvent
import cn.nukkit.form.window.Form
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.network.protocol.ServerSettingsRequestPacket
import cn.nukkit.network.protocol.ServerSettingsResponsePacket

class ServerSettingsRequestProcessor : DataPacketProcessor<ServerSettingsRequestPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: ServerSettingsRequestPacket) {
        val settingsRequestEvent =
            PlayerServerSettingsRequestEvent(playerHandle.player, HashMap(playerHandle.serverSettings))
        playerHandle.player.getServer().getPluginManager().callEvent(settingsRequestEvent)

        if (!settingsRequestEvent.isCancelled) {
            settingsRequestEvent.getSettings().forEach { id: Int?, window: Form<*> ->
                val re = ServerSettingsResponsePacket()
                re.formId = id!!
                re.data = window.toJson()
                playerHandle.player.dataPacket(re)
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.SERVER_SETTINGS_REQUEST_PACKET
}
