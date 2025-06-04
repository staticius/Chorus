package org.chorus_oss.chorus.network.connection.netty.codec.packet

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec
import kotlinx.io.Buffer
import kotlinx.io.asSource
import kotlinx.io.buffered
import org.chorus_oss.chorus.network.connection.netty.BedrockPacketWrapper
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.registry.Registries
import org.chorus_oss.chorus.utils.Loggable
import org.chorus_oss.protocol.core.Packet
import org.chorus_oss.protocol.core.PacketRegistry

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
        val wrapper = BedrockPacketWrapper(0, 0, 0, null, null)
        wrapper.packetBuffer = msg.retainedSlice()
        try {
            val index = msg.readerIndex()
            this.decodeHeader(msg, wrapper)
            wrapper.headerLength = msg.readerIndex() - index

//            val codec = PacketRegistry.get(wrapper.packetId) ?: run {
//                log.info("Couldn't find PacketCodec for id: ${wrapper.packetId}")
//                return
//            }
//            val packet = codec.deserialize(ByteBufInputStream(msg).asSource().buffered()) as Packet

            val packetDecoder = Registries.PACKET_DECODER[wrapper.packetId]
            if (packetDecoder == null) {
                log.info("Couldn't find PacketDecoder for PacketID: ${wrapper.packetId}")
                return
            }
            wrapper.packet = packetDecoder.decode(HandleByteBuf.of(msg))
            out.add(wrapper.retain())
        } catch (t: Throwable) {
            log.info("Failed to decode PacketID: ${wrapper.packetId}", t)
            throw t
        } finally {
            wrapper.release()
        }
    }

    abstract fun encodeHeader(buf: ByteBuf, msg: BedrockPacketWrapper)

    abstract fun decodeHeader(buf: ByteBuf, msg: BedrockPacketWrapper)

    companion object : Loggable {
        const val NAME: String = "bedrock-packet-codec"
    }
}
