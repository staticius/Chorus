package org.chorus.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.entity.EntityHuman.setSkin
import cn.nukkit.event.player.PlayerChangeSkinEvent
import cn.nukkit.event.player.PlayerServerSettingsRequestEvent.getSettings
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.PlayerSkinPacket
import cn.nukkit.network.protocol.ProtocolInfo
import lombok.extern.slf4j.Slf4j
import java.util.concurrent.TimeUnit

@Slf4j
class PlayerSkinProcessor : DataPacketProcessor<PlayerSkinPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: PlayerSkinPacket) {
        val player = playerHandle.player
        val skin = pk.skin

        if (!skin.isValid()) {
            PlayerSkinProcessor.log.warn(playerHandle.username + ": PlayerSkinPacket with invalid skin")
            return
        }

        if (player.getServer().getSettings().playerSettings().forceSkinTrusted()) {
            skin.setTrusted(true)
        }

        val playerChangeSkinEvent = PlayerChangeSkinEvent(player, skin)
        val tooQuick = TimeUnit.SECONDS.toMillis(
            player.getServer().getSettings().playerSettings().skinChangeCooldown()
        ) > System.currentTimeMillis() - player.lastSkinChange
        if (tooQuick) {
            playerChangeSkinEvent.isCancelled = true
            PlayerSkinProcessor.log.warn("Player " + playerHandle.username + " change skin too quick!")
        }
        player.getServer().getPluginManager().callEvent(playerChangeSkinEvent)
        if (!playerChangeSkinEvent.isCancelled) {
            player.lastSkinChange = System.currentTimeMillis()
            player.setSkin(skin)
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.PLAYER_SKIN_PACKET
}
