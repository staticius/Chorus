package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf

class ChangeDimensionPacket : DataPacket() {
    @JvmField
    var dimension: Int = 0

    @JvmField
    var x: Float = 0f

    @JvmField
    var y: Float = 0f

    @JvmField
    var z: Float = 0f

    @JvmField
    var respawn: Boolean = false

    @JvmField
    var loadingScreenId: Int? = null

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(this.dimension)
        byteBuf.writeVector3f(this.x, this.y, this.z)
        byteBuf.writeBoolean(this.respawn)
        byteBuf.writeBoolean(this.loadingScreenId != null)
        if (this.loadingScreenId != null) {
            byteBuf.writeIntLE(loadingScreenId!!)
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.CHANGE_DIMENSION_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
