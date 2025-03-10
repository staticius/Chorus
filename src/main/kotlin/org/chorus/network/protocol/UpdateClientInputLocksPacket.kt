package org.chorus.network.protocol

import org.chorus.math.Vector3f
import org.chorus.network.connection.util.HandleByteBuf


(doNotUseGetters = true, callSuper = false)



(doNotUseGetters = true)


class UpdateClientInputLocksPacket : DataPacket() {
    var lockComponentData: Int = 0
    var serverPosition: Vector3f? = null

    override fun decode(byteBuf: HandleByteBuf) {
        this.lockComponentData = byteBuf.readVarInt()
        this.serverPosition = byteBuf.readVector3f()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(lockComponentData)
        byteBuf.writeVector3f(serverPosition!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.UPDATE_CLIENT_INPUT_LOCKS
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
