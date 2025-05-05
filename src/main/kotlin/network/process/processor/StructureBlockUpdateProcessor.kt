package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.PlayerHandle
import org.chorus_oss.chorus.block.property.CommonBlockProperties
import org.chorus_oss.chorus.blockentity.BlockEntityStructBlock
import org.chorus_oss.chorus.math.Vector3
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.StructureBlockUpdatePacket

class StructureBlockUpdateProcessor : DataPacketProcessor<StructureBlockUpdatePacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: StructureBlockUpdatePacket) {
        if (playerHandle.player.isOp && playerHandle.player.isCreative) {
            val blockEntity = playerHandle.player.level!!.getBlockEntity(
                Vector3(
                    pk.blockPosition.x.toDouble(),
                    pk.blockPosition.y.toDouble(),
                    pk.blockPosition.z.toDouble()
                )
            )
            if (blockEntity is BlockEntityStructBlock) {
                val sBlock = blockEntity.levelBlock
                sBlock.setPropertyValue(CommonBlockProperties.STRUCTURE_BLOCK_TYPE, pk.editorData.type)
                blockEntity.updateSetting(pk)
                playerHandle.player.level!!.setBlock(blockEntity.position, sBlock, true)
                blockEntity.spawnTo(playerHandle.player)
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.STRUCTURE_BLOCK_UPDATE_PACKET
}
