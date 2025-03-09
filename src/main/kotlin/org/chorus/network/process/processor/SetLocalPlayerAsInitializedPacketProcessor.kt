package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.network.protocol.SetLocalPlayerAsInitializedPacket
import lombok.extern.slf4j.Slf4j

@Slf4j
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
}
