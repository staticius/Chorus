package cn.nukkit.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.blockentity.BlockEntityStructBlock
import cn.nukkit.math.Vector3
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.ProtocolInfo
import cn.nukkit.network.protocol.StructureBlockUpdatePacket

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
                sBlock!!.setPropertyValue(CommonBlockProperties.STRUCTURE_BLOCK_TYPE, pk.editorData.type)
                blockEntity.updateSetting(pk)
                playerHandle.player.level!!.setBlock(blockEntity.position, sBlock, true)
                blockEntity.spawnTo(playerHandle.player)
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.STRUCTURE_BLOCK_UPDATE_PACKET
}
