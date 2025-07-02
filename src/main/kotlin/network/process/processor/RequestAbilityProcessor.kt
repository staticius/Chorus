package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.AdventureSettings
import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.event.player.PlayerIllegalFlightEvent
import org.chorus_oss.chorus.event.player.PlayerKickEvent
import org.chorus_oss.chorus.event.player.PlayerToggleFlightEvent
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.RequestAbilityPacket
import org.chorus_oss.chorus.network.protocol.types.PlayerAbility
import org.chorus_oss.chorus.utils.Loggable


class RequestAbilityProcessor : DataPacketProcessor<RequestAbilityPacket>() {
    override fun handle(player: Player, pk: RequestAbilityPacket) {
        val player = player.player
        val ability = pk.ability
        if (ability != PlayerAbility.FLYING) {
            log.info("[" + player.getEntityName() + "] has tried to trigger " + ability + " ability " + (if (pk.boolValue) "on" else "off"))
            return
        }

        if (!Server.instance.allowFlight && pk.boolValue && !player.adventureSettings[AdventureSettings.Type.ALLOW_FLIGHT]
        ) {
            val pife = PlayerIllegalFlightEvent(player)
            Server.instance.pluginManager.callEvent(pife)
            if (!pife.isKick) return
            player.kick(PlayerKickEvent.Reason.FLYING_DISABLED, "Flying is not enabled on this server")
            return
        }

        val playerToggleFlightEvent = PlayerToggleFlightEvent(player, pk.boolValue)
        Server.instance.pluginManager.callEvent(playerToggleFlightEvent)
        if (playerToggleFlightEvent.cancelled) {
            player.adventureSettings.update()
        } else {
            player.adventureSettings[AdventureSettings.Type.FLYING] = playerToggleFlightEvent.isFlying
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.REQUEST_ABILITY_PACKET

    companion object : Loggable
}
