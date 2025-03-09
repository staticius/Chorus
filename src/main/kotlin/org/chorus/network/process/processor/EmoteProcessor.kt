package org.chorus.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.EmotePacket
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.utils.UUIDValidator
import lombok.extern.slf4j.Slf4j

@Slf4j
class EmoteProcessor : DataPacketProcessor<EmotePacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: EmotePacket) {
        if (!playerHandle.player.spawned) {
            return
        }
        if (pk.runtimeId != playerHandle.player.getId()) {
            EmoteProcessor.log.warn(
                "{} sent EmotePacket with invalid entity id: {} != {}",
                playerHandle.username,
                pk.runtimeId,
                playerHandle.player.getId()
            )
            return
        }
        if (!UUIDValidator.isValidUUID(pk.emoteID)) {
            EmoteProcessor.log.warn("{} sent EmotePacket with invalid emoteId: {}", playerHandle.username, pk.emoteID)
            return
        }

        for (viewer in playerHandle.player.getViewers().values) {
            viewer.dataPacket(pk)
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.EMOTE_PACKET
}
