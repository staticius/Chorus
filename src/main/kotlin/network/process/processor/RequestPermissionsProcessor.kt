package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.event.player.PlayerHackDetectedEvent
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.RequestPermissionsPacket

class RequestPermissionsProcessor : DataPacketProcessor<RequestPermissionsPacket>() {
    override fun handle(player: Player, pk: RequestPermissionsPacket) {
        if (!player.player.isOp) {
            val event =
                PlayerHackDetectedEvent(player.player, PlayerHackDetectedEvent.HackType.PERMISSION_REQUEST)
            Server.instance.pluginManager.callEvent(event)

            if (event.isKick) player.player.kick("Illegal permission operation", true)

            return
        }
        val player = pk.targetPlayer
        if (player != null && player.isOnline) {
            val customPermissions = pk.parseCustomPermissions()
            for (controllableAbility in RequestPermissionsPacket.CONTROLLABLE_ABILITIES) {
                player.adventureSettings[controllableAbility] = customPermissions.contains(controllableAbility)
            }
            player.adventureSettings.playerPermission = pk.permissions
            player.adventureSettings.update()
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.REQUEST_PERMISSIONS_PACKET
}
