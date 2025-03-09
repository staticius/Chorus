package org.chorus.network.process.processor

import cn.nukkit.AdventureSettings
import cn.nukkit.PlayerHandle
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.entity.EntityHuman.getName
import cn.nukkit.event.player.PlayerIllegalFlightEvent
import cn.nukkit.event.player.PlayerKickEvent
import cn.nukkit.event.player.PlayerToggleFlightEvent
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.network.protocol.RequestAbilityPacket
import cn.nukkit.network.protocol.types.PlayerAbility
import lombok.extern.slf4j.Slf4j

@Slf4j
class RequestAbilityProcessor : DataPacketProcessor<RequestAbilityPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: RequestAbilityPacket) {
        val player = playerHandle.player
        val ability = pk.ability
        if (ability != PlayerAbility.FLYING) {
            RequestAbilityProcessor.log.info("[" + player.getName() + "] has tried to trigger " + ability + " ability " + (if (pk.boolValue) "on" else "off"))
            return
        }

        if (!player.getServer()
                .getAllowFlight() && pk.boolValue && !player.adventureSettings[AdventureSettings.Type.ALLOW_FLIGHT]
        ) {
            val pife = PlayerIllegalFlightEvent(player)
            player.getServer().getPluginManager().callEvent(pife)
            if (!pife.isKick) return
            player.kick(PlayerKickEvent.Reason.FLYING_DISABLED, "Flying is not enabled on this server")
            return
        }

        val playerToggleFlightEvent = PlayerToggleFlightEvent(player, pk.boolValue)
        player.getServer().getPluginManager().callEvent(playerToggleFlightEvent)
        if (playerToggleFlightEvent.isCancelled) {
            player.adventureSettings.update()
        } else {
            player.adventureSettings[AdventureSettings.Type.FLYING] = playerToggleFlightEvent.isFlying
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.REQUEST_ABILITY_PACKET
}
