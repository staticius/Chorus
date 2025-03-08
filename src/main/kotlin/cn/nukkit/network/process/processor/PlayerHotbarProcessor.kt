package cn.nukkit.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.inventory.SpecialWindowId
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.PlayerHotbarPacket
import cn.nukkit.network.protocol.ProtocolInfo

class PlayerHotbarProcessor : DataPacketProcessor<PlayerHotbarPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: PlayerHotbarPacket) {
        if (pk.windowId != SpecialWindowId.PLAYER.id) {
            return  //In PE this should never happen
        }
        playerHandle.player.getInventory().equipItem(pk.selectedHotbarSlot)
    }

    override val packetId: Int
        get() = ProtocolInfo.PLAYER_HOTBAR_PACKET
}
