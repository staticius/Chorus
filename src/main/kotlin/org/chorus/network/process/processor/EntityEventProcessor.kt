package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.item.ItemFood
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.EntityEventPacket
import org.chorus.network.protocol.ProtocolInfo

class EntityEventProcessor : DataPacketProcessor<EntityEventPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: EntityEventPacket) {
        val player = playerHandle.player
        if (!player.spawned || !player.isAlive()) {
            return
        }

        if (pk.event == EntityEventPacket.EATING_ITEM) {
            if (pk.data == 0 || pk.eid != player.getId()) {
                return
            }

            val hand = player.getInventory().itemInHand as? ItemFood ?: return

            val predictedData = (hand.runtimeId shl 16) or hand.damage
            if (pk.data != predictedData) {
                return
            }

            pk.eid = player.getId()
            pk.data = predictedData

            player.dataPacket(pk)
            Server.broadcastPacket(player.getViewers().values, pk)
        } else if (pk.event == EntityEventPacket.ENCHANT) {
            if (pk.eid != player.getId()) {
                return
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.ENTITY_EVENT_PACKET
}
