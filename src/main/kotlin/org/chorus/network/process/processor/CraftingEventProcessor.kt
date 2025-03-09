package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.CraftingEventPacket
import org.chorus.network.protocol.ProtocolInfo

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
