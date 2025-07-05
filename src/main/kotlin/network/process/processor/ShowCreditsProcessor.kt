package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.event.player.PlayerTeleportEvent
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.ShowCreditsPacket

class ShowCreditsProcessor : DataPacketProcessor<ShowCreditsPacket>() {
    override fun handle(player: Player, pk: ShowCreditsPacket) {
        if (pk.status == ShowCreditsPacket.STATUS_END_CREDITS) {
            if (player.player.showingCredits) {
                player.player.showingCredits = false
                player.player.teleport(
                    player.player.spawn.first!!,
                    PlayerTeleportEvent.TeleportCause.END_PORTAL
                )
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.SHOW_CREDITS_PACKET
}
