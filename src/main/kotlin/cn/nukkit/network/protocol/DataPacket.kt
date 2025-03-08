package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class DataPacket {
    abstract fun decode(byteBuf: HandleByteBuf)

    abstract fun encode(byteBuf: HandleByteBuf)

    abstract fun pid(): Int

    abstract fun handle(handler: PacketHandler)

    companion object {
        val EMPTY_ARRAY: Array<DataPacket?> = arrayOfNulls(0)
    }
}
