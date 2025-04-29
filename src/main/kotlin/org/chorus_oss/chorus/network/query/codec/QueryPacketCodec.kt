package org.chorus_oss.chorus.network.query.codec

import io.netty.buffer.ByteBufAllocator
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageCodec
import org.chorus_oss.chorus.network.query.enveloped.DirectAddressedQueryPacket
import org.chorus_oss.chorus.network.query.packet.HandshakePacket
import org.chorus_oss.chorus.network.query.packet.StatisticsPacket

class QueryPacketCodec : MessageToMessageCodec<DatagramPacket, DirectAddressedQueryPacket>() {
    @Throws(Exception::class)
    override fun encode(
        channelHandlerContext: ChannelHandlerContext,
        packet: DirectAddressedQueryPacket,
        list: MutableList<Any>
    ) {
        try {
            val buf = ByteBufAllocator.DEFAULT.ioBuffer()
            buf.writeByte(packet.content()!!.id.toInt() and 0xFF)
            packet.content()!!.encode(buf)
            list.add(DatagramPacket(buf, packet.recipient(), packet.sender()))
        } finally {
            packet.release()
        }
    }

    @Throws(Exception::class)
    override fun decode(channelHandlerContext: ChannelHandlerContext, packet: DatagramPacket, list: MutableList<Any>) {
        val buf = packet.content()
        if (buf.readableBytes() < 3) {
            // not interested
            return
        }
        buf.markReaderIndex()

        val prefix = ByteArray(2)
        buf.readBytes(prefix)
        if (prefix.contentEquals(QUERY_SIGNATURE)) {
            val id = buf.readUnsignedByte()
            val networkPacket = when (id) {
                HANDSHAKE -> HandshakePacket()
                STATISTICS -> StatisticsPacket()
                else -> {
                    buf.resetReaderIndex()
                    return
                }
            }
            networkPacket.decode(buf)
            list.add(DirectAddressedQueryPacket(networkPacket, packet.recipient(), packet.sender()))
        } else {
            buf.resetReaderIndex()
        }
    }

    companion object {
        private val QUERY_SIGNATURE = byteArrayOf(0xFE.toByte(), 0xFD.toByte())
        private const val HANDSHAKE: Short = 0x09
        private const val STATISTICS: Short = 0x00
    }
}
