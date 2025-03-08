package cn.nukkit.network.protocol.types

import cn.nukkit.network.connection.util.HandleByteBuf

interface EventData {
    val type: EventDataType

    fun write(byteBuf: HandleByteBuf)
}
