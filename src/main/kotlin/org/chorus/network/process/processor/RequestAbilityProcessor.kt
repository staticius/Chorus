package org.chorus.network.process.processor

import org.chorus.AdventureSettings
import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.event.player.PlayerIllegalFlightEvent
import org.chorus.event.player.PlayerKickEvent
import org.chorus.event.player.PlayerToggleFlightEvent
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.network.protocol.RequestAbilityPacket
import org.chorus.network.protocol.types.PlayerAbility
import org.chorus.utils.Loggable


class RequestAbilityProcessor : DataPacketProcessor<RequestAbilityPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: RequestAbilityPacket) {
        val player = playerHandle.player
        val ability = pk.ability
        if (ability != PlayerAbility.FLYING) {
            RequestAbilityProcessor.log.info("[" + player.getName() + "] has tried to trigger " + ability + " ability " + (if (pk.boolValue) "on" else "off"))
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
        if (playerToggleFlightEvent.isCancelled) {
            player.adventureSettings.update()
        } else {
            player.adventureSettings[AdventureSettings.Type.FLYING] = playerToggleFlightEvent.isFlying
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.REQUEST_ABILITY_PACKET

    companion object : Loggable
}
