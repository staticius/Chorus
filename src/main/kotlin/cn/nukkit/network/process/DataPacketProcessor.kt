package cn.nukkit.network.process

import cn.nukkit.PlayerHandle
import cn.nukkit.network.protocol.DataPacket
import cn.nukkit.network.protocol.ProtocolInfo

/**
 * A DataPacketProcessor is used to handle a specific type of DataPacket. <br></br>
 * DataPacketProcessor must be **thread-safe**. <br></br>
 * <hr></hr>
 * Why not interfaces? Hotspot C2 JIT cannot handle so many classes that impl the same interface, it makes the
 * performance lower.
 */
abstract class DataPacketProcessor<T : DataPacket?> {
    abstract fun handle(playerHandle: PlayerHandle, pk: T)

    abstract val packetId: Int

    val protocol: Int
        get() = ProtocolInfo.CURRENT_PROTOCOL
}
