package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.BlockSyncType
import lombok.*






class UpdateBlockSyncedPacket : UpdateBlockPacket() {
    var actorUniqueId: Long = 0
    var updateType: BlockSyncType? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        super.encode(byteBuf)
        byteBuf.writeUnsignedVarLong(actorUniqueId)
        byteBuf.writeUnsignedVarLong(updateType!!.ordinal().toLong())
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.UPDATE_BLOCK_SYNCED_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
