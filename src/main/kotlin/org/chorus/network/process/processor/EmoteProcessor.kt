package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.EmotePacket
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.utils.Loggable
import org.chorus.utils.UUIDValidator


class EmoteProcessor : DataPacketProcessor<EmotePacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: EmotePacket) {
        if (!playerHandle.player.spawned) {
            return
        }
        if (pk.runtimeId != playerHandle.player.getRuntimeID()) {
            EmoteProcessor.log.warn(
                "{} sent EmotePacket with invalid entity id: {} != {}",
                playerHandle.username,
                pk.runtimeId,
                playerHandle.player.getRuntimeID()
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

    companion object : Loggable
}
