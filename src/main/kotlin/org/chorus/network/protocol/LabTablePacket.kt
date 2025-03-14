package org.chorus.network.protocol

import org.chorus.math.BlockVector3
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.LabTableReactionType
import org.chorus.network.protocol.types.LabTableType


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
