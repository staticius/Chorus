package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.event.player.PlayerHackDetectedEvent
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.network.protocol.RequestPermissionsPacket

class RequestPermissionsProcessor : DataPacketProcessor<RequestPermissionsPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: RequestPermissionsPacket) {
        if (!playerHandle.player.isOp) {
            val event =
                PlayerHackDetectedEvent(playerHandle.player, PlayerHackDetectedEvent.HackType.PERMISSION_REQUEST)
            Server.instance.pluginManager.callEvent(event)

            if (event.isKick) playerHandle.player.kick("Illegal permission operation", true)

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
