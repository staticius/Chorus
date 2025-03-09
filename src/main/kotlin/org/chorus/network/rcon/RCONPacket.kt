package org.chorus.network.rcon

import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * A data structure representing an RCON packet.
 *
 * @author Tee7even
 */
class RCONPacket {
    val id: Int
    val type: Int
    val payload: ByteArray

    constructor(id: Int, type: Int, payload: ByteArray) {
        this.id = id
        this.type = type
        this.payload = payload
    }

    constructor(buffer: ByteBuffer) {
        val size = buffer.getInt()

        this.id = buffer.getInt()
        this.type = buffer.getInt()
        this.payload = ByteArray(size - 10)
        buffer[payload]

        buffer[ByteArray(2)]
    }

    fun toBuffer(): ByteBuffer {
        val buffer = ByteBuffer.allocate(payload.size + 14)
        buffer.order(ByteOrder.LITTLE_ENDIAN)

        buffer.putInt(payload.size + 10)
        buffer.putInt(this.id)
        buffer.putInt(this.type)
        buffer.put(this.payload)

        buffer.put(0.toByte())
        buffer.put(0.toByte())

        buffer.flip()
        return buffer
    }
}
