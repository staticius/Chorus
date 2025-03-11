package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.event.player.PlayerChangeSkinEvent
import org.chorus.event.player.PlayerServerSettingsRequestEvent.getSettings
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.PlayerSkinPacket
import org.chorus.network.protocol.ProtocolInfo

import java.util.concurrent.TimeUnit


class PlayerSkinProcessor : DataPacketProcessor<PlayerSkinPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: PlayerSkinPacket) {
        val player = playerHandle.player
        val skin = pk.skin

        if (!skin.isValid()) {
            PlayerSkinProcessor.log.warn(playerHandle.username + ": PlayerSkinPacket with invalid skin")
            return
        }

        if (Server.instance.getSettings().playerSettings().forceSkinTrusted()) {
            skin.setTrusted(true)
        }

        val playerChangeSkinEvent = PlayerChangeSkinEvent(player, skin)
        val tooQuick = TimeUnit.SECONDS.toMillis(
            Server.instance.getSettings().playerSettings().skinChangeCooldown()
        ) > System.currentTimeMillis() - player.lastSkinChange
        if (tooQuick) {
            playerChangeSkinEvent.isCancelled = true
            PlayerSkinProcessor.log.warn("Player " + playerHandle.username + " change skin too quick!")
        }
        Server.instance.getPluginManager().callEvent(playerChangeSkinEvent)
        if (!playerChangeSkinEvent.isCancelled) {
            player.lastSkinChange = System.currentTimeMillis()
            player.setSkin(skin)
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.PLAYER_SKIN_PACKET
}
