package cn.nukkit.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.block.BlockLectern
import cn.nukkit.blockentity.BlockEntityLectern
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.event.block.LecternPageChangeEvent
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.LecternUpdatePacket
import cn.nukkit.network.protocol.ProtocolInfo

class LecternUpdateProcessor : DataPacketProcessor<LecternUpdatePacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: LecternUpdatePacket) {
        val blockPosition = pk.blockPosition
        val blockEntityLectern = playerHandle.player.level!!.getBlockEntity(blockPosition)
        if (blockEntityLectern is BlockEntityLectern) {
            val lecternPageChangeEvent = LecternPageChangeEvent(playerHandle.player, blockEntityLectern, pk.page)
            playerHandle.player.getServer().getPluginManager().callEvent(lecternPageChangeEvent)
            if (!lecternPageChangeEvent.isCancelled) {
                blockEntityLectern.rawPage = lecternPageChangeEvent.newRawPage
                blockEntityLectern.spawnToAll()
                val blockLectern = blockEntityLectern.block
                if (blockLectern is BlockLectern) {
                    blockLectern.executeRedstonePulse()
                }
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.LECTERN_UPDATE_PACKET
}
