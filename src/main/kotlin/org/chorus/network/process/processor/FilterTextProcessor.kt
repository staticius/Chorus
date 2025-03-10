package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.FilterTextPacket
import org.chorus.network.protocol.ProtocolInfo



class FilterTextProcessor : DataPacketProcessor<FilterTextPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: FilterTextPacket) {
        val player = playerHandle.player
        if (pk.text == null || pk.text.length > 64) {
            FilterTextProcessor.log.debug(playerHandle.username + ": FilterTextPacket with too long text")
            return
        }
        val textResponsePacket = FilterTextPacket()

        textResponsePacket.text = pk.text
        textResponsePacket.isFromServer = true
        player.dataPacket(textResponsePacket)
    }

    override val packetId: Int
        get() = ProtocolInfo.FILTER_TEXT_PACKET
}
