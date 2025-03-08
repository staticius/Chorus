package cn.nukkit.network.protocol

import cn.nukkit.math.BlockVector3
import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.network.protocol.types.LabTableReactionType
import cn.nukkit.network.protocol.types.LabTableType
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class LabTablePacket : DataPacket() {
    var actionType: LabTableType? = null
    var blockPosition: BlockVector3? = null
    var reactionType: LabTableReactionType? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(actionType!!.ordinal.toByte().toInt())
        byteBuf.writeBlockVector3(blockPosition!!)
        byteBuf.writeByte(reactionType!!.ordinal.toByte().toInt())
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.LAB_TABLE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
