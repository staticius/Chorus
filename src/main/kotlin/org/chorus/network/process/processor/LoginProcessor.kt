package org.chorus.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.event.player.PlayerDuplicatedLoginEvent
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.LoginPacket
import cn.nukkit.network.protocol.ProtocolInfo

class LoginProcessor : DataPacketProcessor<LoginPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: LoginPacket) {
        val player = playerHandle.player
        if (!player.session.isAuthenticated()) {
            return
        }

        val event = PlayerDuplicatedLoginEvent(player)
        player.getServer().getPluginManager().callEvent(event)

        if (event.isCancelled) {
            return
        }

        player.close("§cPacket handling error")
    }

    override val packetId: Int
        get() = ProtocolInfo.LOGIN_PACKET
}
