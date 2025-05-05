package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.PlayerHandle
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.SetLocalPlayerAsInitializedPacket
import org.chorus_oss.chorus.utils.Loggable


class SetLocalPlayerAsInitializedPacketProcessor : DataPacketProcessor<SetLocalPlayerAsInitializedPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: SetLocalPlayerAsInitializedPacket) {
        val player = playerHandle.player
        SetLocalPlayerAsInitializedPacketProcessor.log.debug(
            "receive SetLocalPlayerAsInitializedPacket for {}",
            player.playerInfo.username
        )
        playerHandle.onPlayerLocallyInitialized()
    }

    override val packetId: Int
        get() = ProtocolInfo.SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET

    companion object : Loggable
}
