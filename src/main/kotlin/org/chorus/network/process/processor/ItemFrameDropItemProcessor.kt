package org.chorus.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.blockentity.BlockEntityItemFrame
import cn.nukkit.math.Vector3
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.ItemFrameDropItemPacket
import cn.nukkit.network.protocol.ProtocolInfo

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
