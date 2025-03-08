package cn.nukkit.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.event.player.PlayerTeleportEvent
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.network.protocol.ShowCreditsPacket

class ShowCreditsProcessor : DataPacketProcessor<ShowCreditsPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: ShowCreditsPacket) {
        if (pk.status == ShowCreditsPacket.STATUS_END_CREDITS) {
            if (playerHandle.showingCredits) {
                playerHandle.player.isShowingCredits = false
                playerHandle.player.teleport(
                    playerHandle.player.spawn.left(),
                    PlayerTeleportEvent.TeleportCause.END_PORTAL
                )
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.SHOW_CREDITS_PACKET
}
