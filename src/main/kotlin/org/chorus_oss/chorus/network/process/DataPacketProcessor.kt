package org.chorus_oss.chorus.network.process

import org.chorus_oss.chorus.PlayerHandle
import org.chorus_oss.chorus.network.protocol.DataPacket
import org.chorus_oss.chorus.network.protocol.ProtocolInfo

/**
 * A DataPacketProcessor is used to handle a specific type of DataPacket. <br></br>
 * DataPacketProcessor must be **thread-safe**. <br></br>
 * <hr></hr>
 * Why not interfaces? Hotspot C2 JIT cannot handle so many classes that impl the same interface, it makes the
 * performance lower.
 */
abstract class DataPacketProcessor<T : DataPacket> {
    abstract fun handle(playerHandle: PlayerHandle, pk: T)

    abstract val packetId: Int

    val protocol: Int
        get() = ProtocolInfo.PROTOCOL_VERSION
}
