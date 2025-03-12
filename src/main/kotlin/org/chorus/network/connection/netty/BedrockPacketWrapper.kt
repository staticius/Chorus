package org.chorus.network.connection.netty

import io.netty.buffer.ByteBuf
import io.netty.util.AbstractReferenceCounted
import io.netty.util.ReferenceCountUtil
import org.chorus.network.protocol.DataPacket


(callSuper = false)


class BedrockPacketWrapper(
    private var packetId: Int,
    private var senderSubClientId: Int,
    private var targetSubClientId: Int,
    private var packet: DataPacket?,
    private var packetBuffer: ByteBuf?
) :
    AbstractReferenceCounted() {
    private var headerLength = 0

    override fun deallocate() {
        ReferenceCountUtil.safeRelease(this.packetBuffer)
        this.packet = null
    }

    override fun touch(hint: Any): BedrockPacketWrapper {
        ReferenceCountUtil.touch(this.packetBuffer)
        return this
    }

    override fun retain(): BedrockPacketWrapper {
        return super.retain() as BedrockPacketWrapper
    }
}
