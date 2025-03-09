package org.chorus.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.blockentity.BlockEntitySpawnable
import cn.nukkit.math.Vector3
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.BlockEntityDataPacket
import cn.nukkit.network.protocol.ProtocolInfo

class BlockEntityDataProcessor : DataPacketProcessor<BlockEntityDataPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: BlockEntityDataPacket) {
        val player = playerHandle.player
        if (!player.spawned || !player.isAlive()) {
            return
        }

        val pos = Vector3(pk.x.toDouble(), pk.y.toDouble(), pk.z.toDouble())
        if (pos.distanceSquared(player.position) > 10000) {
            return
        }
        player.resetInventory()

        val t = player.level!!.getBlockEntity(pos)
        if (t is BlockEntitySpawnable) {
            val nbt = pk.namedTag
            if (!t.updateCompoundTag(nbt, player)) {
                t.spawnTo(player)
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.BLOCK_ENTITY_DATA_PACKET
}
