package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.PlayerHandle
import org.chorus_oss.chorus.blockentity.BlockEntitySpawnable
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.BlockActorDataPacket
import org.chorus_oss.chorus.network.protocol.ProtocolInfo

class BlockEntityDataProcessor : DataPacketProcessor<BlockActorDataPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: BlockActorDataPacket) {
        val player = playerHandle.player
        if (!player.spawned || !player.isAlive()) {
            return
        }

        val pos = pk.blockPosition.asVector3()
        if (pos.distanceSquared(player.position) > 10000) {
            return
        }
        player.resetInventory()

        val t = player.level!!.getBlockEntity(pos)
        if (t is BlockEntitySpawnable) {
            val nbt = pk.actorDataTags
            if (!t.updateCompoundTag(nbt, player)) {
                t.spawnTo(player)
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.BLOCK_ACTOR_DATA_PACKET
}
