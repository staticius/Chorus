package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.PlayerHandle
import org.chorus_oss.chorus.event.player.PlayerTeleportEvent
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.ShowCreditsPacket

class ShowCreditsProcessor : DataPacketProcessor<ShowCreditsPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: ShowCreditsPacket) {
        if (pk.status == ShowCreditsPacket.STATUS_END_CREDITS) {
            if (playerHandle.showingCredits) {
                playerHandle.player.showingCredits = false
                playerHandle.player.teleport(
                    playerHandle.player.spawn.first!!,
                    PlayerTeleportEvent.TeleportCause.END_PORTAL
                )
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.SHOW_CREDITS_PACKET
}
