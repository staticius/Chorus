package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


abstract class DataPacket {
    abstract fun decode(byteBuf: HandleByteBuf)

    abstract fun encode(byteBuf: HandleByteBuf)

    abstract fun pid(): Int

    abstract fun handle(handler: PacketHandler)

    companion object {
        val EMPTY_ARRAY: Array<DataPacket> = emptyArray()
    }
}
