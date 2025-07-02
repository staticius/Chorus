package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.EmotePacket
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.utils.Loggable
import org.chorus_oss.chorus.utils.UUIDValidator


class EmoteProcessor : DataPacketProcessor<EmotePacket>() {
    override fun handle(player: Player, pk: EmotePacket) {
        if (!player.player.spawned) {
            return
        }
        if (pk.runtimeId != player.player.getRuntimeID()) {
            log.warn(
                "{} sent EmotePacket with invalid entity id: {} != {}",
                player.player.getEntityName(),
                pk.runtimeId,
                player.player.getRuntimeID()
            )
            return
        }
        if (!UUIDValidator.isValidUUID(pk.emoteID)) {
            log.warn(
                "{} sent EmotePacket with invalid emoteId: {}",
                player.player.getEntityName(),
                pk.emoteID
            )
            return
        }

        for (viewer in player.player.viewers.values) {
            viewer.dataPacket(pk)
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.EMOTE_PACKET

    companion object : Loggable
}
