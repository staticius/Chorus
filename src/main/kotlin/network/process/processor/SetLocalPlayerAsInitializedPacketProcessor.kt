package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.SetLocalPlayerAsInitializedPacket
import org.chorus_oss.chorus.utils.Loggable


class SetLocalPlayerAsInitializedPacketProcessor : DataPacketProcessor<SetLocalPlayerAsInitializedPacket>() {
    override fun handle(player: Player, pk: SetLocalPlayerAsInitializedPacket) {
        val player = player.player
        log.debug(
            "receive SetLocalPlayerAsInitializedPacket for {}",
            player.playerInfo.username
        )
        player.player.onPlayerLocallyInitialized()
    }

    override val packetId: Int
        get() = ProtocolInfo.SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET

    companion object : Loggable
}
