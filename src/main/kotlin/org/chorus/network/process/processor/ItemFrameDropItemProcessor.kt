package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.blockentity.BlockEntityItemFrame
import org.chorus.math.Vector3
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.ItemFrameDropItemPacket
import org.chorus.network.protocol.ProtocolInfo

class ItemFrameDropItemProcessor : DataPacketProcessor<ItemFrameDropItemPacket>() {
    // PowerNukkit Note: This packed is not being sent anymore since 1.16.210
    override fun handle(playerHandle: PlayerHandle, pk: ItemFrameDropItemPacket) {
        val vector3 = Vector3(pk.x.toDouble(), pk.y.toDouble(), pk.z.toDouble())
        if (vector3.distanceSquared(playerHandle.player.position) < 1000) {
            val itemFrame = playerHandle.player.level!!.getBlockEntity(vector3)
            if (itemFrame is BlockEntityItemFrame) {
                itemFrame.dropItem(playerHandle.player)
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.ITEM_FRAME_DROP_ITEM_PACKET
}
