package cn.nukkit.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.FilterTextPacket
import cn.nukkit.network.protocol.ProtocolInfo
import lombok.extern.slf4j.Slf4j

@Slf4j
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
