package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.inventory.SpecialWindowId
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.PlayerHotbarPacket
import org.chorus.network.protocol.ProtocolInfo

class PlayerHotbarProcessor : DataPacketProcessor<PlayerHotbarPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: PlayerHotbarPacket) {
        if (pk.windowId != SpecialWindowId.PLAYER.id) {
            return  //In PE this should never happen
        }
        playerHandle.player.inventory.equipItem(pk.selectedHotbarSlot)
    }

    override val packetId: Int
        get() = ProtocolInfo.PLAYER_HOTBAR_PACKET
}
