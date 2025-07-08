package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.DataPacket
import org.chorus_oss.chorus.network.PacketHandler
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf


open class UpdateBlockPacket : DataPacket() {
    @JvmField
    var x: Int = 0

    @JvmField
    var z: Int = 0

    @JvmField
    var y: Int = 0

    @JvmField
    var blockRuntimeId: Int = 0

    @JvmField
    var flags: Int = 0
    var dataLayer: Int = 0

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBlockVector3(x, y, z)
        byteBuf.writeUnsignedVarInt(blockRuntimeId)
        byteBuf.writeUnsignedVarInt(flags)
        byteBuf.writeUnsignedVarInt(dataLayer)
    }

    override fun pid(): Int {
        return ProtocolInfo.UPDATE_BLOCK_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    override fun toString(): String {
        return "UpdateBlockPacket(x=$x, y=$y, z=$z, blockRuntimeId=$blockRuntimeId, flags=$flags, dataLayer=$dataLayer)"
    }

    companion object {
        const val FLAG_NONE: Int = 0
        const val FLAG_NEIGHBORS: Int = 1
        const val FLAG_NETWORK: Int = 2
        const val FLAG_NOGRAPHIC: Int = 4
        const val FLAG_PRIORITY: Int = 8
        const val FLAG_ALL: Int = (FLAG_NEIGHBORS or FLAG_NETWORK)
        const val FLAG_ALL_PRIORITY: Int = (FLAG_ALL or FLAG_PRIORITY)
    }
}
