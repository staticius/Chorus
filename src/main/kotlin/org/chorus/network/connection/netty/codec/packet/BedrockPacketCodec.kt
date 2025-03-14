package org.chorus.network.connection.netty.codec.packet

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec
import io.netty.util.internal.logging.InternalLogger
import io.netty.util.internal.logging.InternalLoggerFactory
import org.chorus.network.connection.netty.BedrockPacketWrapper
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.registry.Registries

abstract class BedrockPacketCodec : MessageToMessageCodec<ByteBuf, BedrockPacketWrapper>() {
    @Throws(Exception::class)
    override fun encode(ctx: ChannelHandlerContext, msg: BedrockPacketWrapper, out: MutableList<Any>) {
        if (msg.packetBuffer != null) {
            // We have a pre-encoded packet buffer, just use that.
            out.add(msg.retain())
        } else {
            val buf = ctx.alloc().buffer(128)
            try {
                val packet = msg.packet
                msg.packetId = packet!!.pid()
                encodeHeader(buf, msg)
                packet.encode(HandleByteBuf.of(buf))
                msg.packetBuffer = buf.retain()
                out.add(msg.retain())
            } catch (t: Throwable) {
                log.error("Error encoding packet {}", msg.packet, t)
            } finally {
                buf.release()
            }
        }
    }

    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, msg: ByteBuf, out: MutableList<Any>) {
        val wrapper = BedrockPacketWrapper()
        wrapper.packetBuffer = msg.retainedSlice()
        try {
            val index = msg.readerIndex()
            this.decodeHeader(msg, wrapper)
            wrapper.headerLength = msg.readerIndex() - index
            val dataPacket = Registries.PACKET[wrapper.packetId]
            if (dataPacket == null) {
                log.info("Failed to decode packet for packetId {}", wrapper.packetId)
                return
            }
            dataPacket.decode(HandleByteBuf.Companion.of(msg))
            wrapper.packet = dataPacket
            out.add(wrapper.retain())
        } catch (t: Throwable) {
            log.info("Failed to decode packet", t)
            throw t
        } finally {
            wrapper.release()
        }
    }

    abstract fun encodeHeader(buf: ByteBuf, msg: BedrockPacketWrapper)

    abstract fun decodeHeader(buf: ByteBuf, msg: BedrockPacketWrapper)

    companion object {
        const val NAME: String = "bedrock-packet-codec"
        private val log: InternalLogger = InternalLoggerFactory.getInstance(BedrockPacketCodec::class.java)
    }
}
