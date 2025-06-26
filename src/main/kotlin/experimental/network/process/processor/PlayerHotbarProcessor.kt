package org.chorus_oss.chorus.experimental.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.inventory.SpecialWindowId
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.PlayerHotbarPacket
import org.chorus_oss.chorus.network.protocol.ProtocolInfo

class PlayerHotbarProcessor : DataPacketProcessor<PlayerHotbarPacket>() {
    override fun handle(player: Player, pk: PlayerHotbarPacket) {
        if (pk.windowId != SpecialWindowId.PLAYER.id) {
            return  //In PE this should never happen
        }
        player.player.inventory.equipItem(pk.selectedHotbarSlot)
    }

    override val packetId: Int
        get() = ProtocolInfo.PLAYER_HOTBAR_PACKET
}
