package cn.nukkit.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.CraftingEventPacket
import cn.nukkit.network.protocol.ProtocolInfo

class CraftingEventProcessor : DataPacketProcessor<CraftingEventPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: CraftingEventPacket) {
        val player = playerHandle.player
        //todo check craft
        /*if (player.craftingType == Player.CRAFTING_BIG && pk.type == CraftingEventPacket.TYPE_WORKBENCH
                || player.craftingType == Player.CRAFTING_SMALL && pk.type == CraftingEventPacket.TYPE_INVENTORY) {
            if (playerHandle.getCraftingTransaction() != null) {
                playerHandle.getCraftingTransaction().setReadyToExecute(true);
                if (playerHandle.getCraftingTransaction().getPrimaryOutput() == null) {
                    playerHandle.getCraftingTransaction().setPrimaryOutput(pk.output[0]);
                }
            }
        }*/
    }

    override val packetId: Int
        get() = ProtocolInfo.CRAFTING_EVENT_PACKET
}
