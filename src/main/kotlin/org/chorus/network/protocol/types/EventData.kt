package org.chorus.network.protocol.types

import org.chorus.network.connection.util.HandleByteBuf

interface EventData {
    val type: EventDataType

    fun write(byteBuf: HandleByteBuf)
}
