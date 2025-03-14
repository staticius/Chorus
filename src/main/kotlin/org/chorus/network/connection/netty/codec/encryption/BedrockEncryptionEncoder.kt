package org.chorus.network.connection.netty.codec.encryption

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder
import io.netty.util.concurrent.FastThreadLocal
import org.chorus.network.connection.netty.BedrockBatchWrapper
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.concurrent.atomic.AtomicLong
import javax.crypto.Cipher
import javax.crypto.SecretKey

class BedrockEncryptionEncoder : MessageToMessageEncoder<BedrockBatchWrapper>() {
    private val packetCounter = AtomicLong()
    private val key: SecretKey? = null
    private val cipher: Cipher? = null

    @Throws(Exception::class)
    override fun encode(ctx: ChannelHandlerContext, `in`: BedrockBatchWrapper, out: MutableList<Any>) {
        val buf = ctx.alloc().ioBuffer(`in`.compressed!!.readableBytes() + 8)
        try {
            val trailer = ByteBuffer.wrap(
                generateTrailer(
                    `in`.compressed!!,
                    key!!, this.packetCounter
                )
            )
            val inBuffer = `in`.compressed!!.nioBuffer()
            val outBuffer = buf.nioBuffer(0, `in`.compressed!!.readableBytes() + 8)

            var index = cipher!!.update(inBuffer, outBuffer)
            index += cipher.update(trailer, outBuffer)

            buf.writerIndex(index)
            `in`.compressed = buf.retain()
            out.add(`in`.retain())
        } finally {
            buf.release()
        }
    }

    companion object {
        const val NAME: String = "bedrock-encryption-encoder"
        private val DIGEST: FastThreadLocal<MessageDigest> = object : FastThreadLocal<MessageDigest>() {
            override fun initialValue(): MessageDigest {
                try {
                    return MessageDigest.getInstance("SHA-256")
                } catch (e: Exception) {
                    throw AssertionError(e)
                }
            }
        }

        fun generateTrailer(buf: ByteBuf, key: SecretKey, counter: AtomicLong): ByteArray {
            val digest = DIGEST.get()
            val counterBuf = ByteBufAllocator.DEFAULT.directBuffer(8)
            try {
                counterBuf.writeLongLE(counter.getAndIncrement())
                val keyBuffer = ByteBuffer.wrap(key.encoded)

                digest.update(counterBuf.nioBuffer(0, 8))
                digest.update(buf.nioBuffer(buf.readerIndex(), buf.readableBytes()))
                digest.update(keyBuffer)
                val hash = digest.digest()
                return hash.copyOf(8)
            } finally {
                counterBuf.release()
                digest.reset()
            }
        }
    }
}
