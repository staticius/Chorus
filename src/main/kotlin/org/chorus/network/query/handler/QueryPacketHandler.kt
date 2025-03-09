package org.chorus.network.query.handler

import org.chorus.network.query.QueryEventListener
import org.chorus.network.query.enveloped.DirectAddressedQueryPacket
import org.chorus.network.query.packet.HandshakePacket
import org.chorus.network.query.packet.StatisticsPacket
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class QueryPacketHandler(private val listener: QueryEventListener) :
    SimpleChannelInboundHandler<DirectAddressedQueryPacket>() {
    private val timer = Timer("QueryRegenerationTicker")
    private var lastToken: ByteArray
    private val token = ByteArray(16)


    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, packet: DirectAddressedQueryPacket) {
        if (packet.content() is HandshakePacket) {
            val handshake = packet.content() as HandshakePacket
            handshake.token = getTokenString(packet.sender()!!)
            ctx.writeAndFlush(
                DirectAddressedQueryPacket(handshake, packet.sender(), packet.recipient()),
                ctx.voidPromise()
            )
        }
        if (packet.content() is StatisticsPacket) {
            val statistics = packet.content() as StatisticsPacket
            if (statistics.token != getTokenInt(packet.sender()!!)) {
                return
            }

            val data = listener.onQuery(packet.sender())

            if (statistics.isFull) {
                statistics.payload = data!!.longQuery
            } else {
                statistics.payload = data!!.shortQuery
            }
            ctx.writeAndFlush(
                DirectAddressedQueryPacket(statistics, packet.sender(), packet.recipient()),
                ctx.voidPromise()
            )
        }
    }

    fun refreshToken() {
        lastToken = token
        ThreadLocalRandom.current().nextBytes(token)
    }

    private fun getTokenString(socketAddress: InetSocketAddress): String {
        return getTokenInt(socketAddress).toString()
    }

    private fun getTokenInt(socketAddress: InetSocketAddress): Int {
        return ByteBuffer.wrap(getToken(socketAddress)).getInt()
    }

    private fun getToken(socketAddress: InetSocketAddress): ByteArray {
        val digest: MessageDigest
        try {
            digest = MessageDigest.getInstance("MD5")
        } catch (var3: NoSuchAlgorithmException) {
            throw InternalError("MD5 not supported", var3)
        }
        digest.update(socketAddress.toString().toByteArray())
        val digested = digest.digest(token)
        return digested.copyOf(4)
    }
}
