package org.chorus.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.event.player.PlayerHackDetectedEvent
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.network.protocol.RequestPermissionsPacket

class RequestPermissionsProcessor : DataPacketProcessor<RequestPermissionsPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: RequestPermissionsPacket) {
        if (!playerHandle.player.isOp) {
            val event =
                PlayerHackDetectedEvent(playerHandle.player, PlayerHackDetectedEvent.HackType.PERMISSION_REQUEST)
            playerHandle.player.getServer().getPluginManager().callEvent(event)

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
