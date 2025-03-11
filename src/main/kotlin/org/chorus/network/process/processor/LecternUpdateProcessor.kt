package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.block.BlockLectern
import org.chorus.blockentity.BlockEntityLectern
import org.chorus.event.block.LecternPageChangeEvent
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.LecternUpdatePacket
import org.chorus.network.protocol.ProtocolInfo

class LecternUpdateProcessor : DataPacketProcessor<LecternUpdatePacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: LecternUpdatePacket) {
        val blockPosition = pk.blockPosition
        val blockEntityLectern = playerHandle.player.level!!.getBlockEntity(blockPosition)
        if (blockEntityLectern is BlockEntityLectern) {
            val lecternPageChangeEvent = LecternPageChangeEvent(playerHandle.player, blockEntityLectern, pk.page)
            playerHandle.Server.instance.getPluginManager().callEvent(lecternPageChangeEvent)
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
