package org.chorus.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.network.protocol.SetLocalPlayerAsInitializedPacket
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
