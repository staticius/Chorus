package org.chorus.network.connection.netty.codec.encryption

import cn.nukkit.network.connection.netty.BedrockBatchWrapper
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.CorruptedFrameException
import io.netty.handler.codec.MessageToMessageDecoder
import lombok.RequiredArgsConstructor
import java.util.concurrent.atomic.AtomicLong
import javax.crypto.Cipher
import javax.crypto.SecretKey

@RequiredArgsConstructor
class BedrockEncryptionDecoder : MessageToMessageDecoder<BedrockBatchWrapper>() {
    private val packetCounter = AtomicLong()
    private val key: SecretKey? = null
    private val cipher: Cipher? = null

    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, msg: BedrockBatchWrapper, out: MutableList<Any>) {
        val inBuffer = msg.compressed.nioBuffer()
        val outBuffer = inBuffer.duplicate()

        // Copy-safe so we can use the same buffer.
        cipher!!.update(inBuffer, outBuffer)

        val output = msg.compressed.readSlice(msg.compressed.readableBytes() - 8)

        if (VALIDATE) {
            val trailer = msg.compressed.readSlice(8)

            val actual = ByteArray(8)
            trailer.readBytes(actual)

            val expected: ByteArray = BedrockEncryptionEncoder.Companion.generateTrailer(
                output,
                key!!, this.packetCounter
            )

            if (!expected.contentEquals(actual)) {
                throw CorruptedFrameException("Invalid encryption trailer")
            }
        }

        msg.compressed = output.retain()
        out.add(msg.retain())
    }

    companion object {
        const val NAME: String = "bedrock-encryption-decoder"
        private val VALIDATE = java.lang.Boolean.getBoolean("cloudburst.validateEncryption")
    }
}
