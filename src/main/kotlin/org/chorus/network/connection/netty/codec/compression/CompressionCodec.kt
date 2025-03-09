package org.chorus.network.connection.netty.codec.compression

import cn.nukkit.Server
import cn.nukkit.network.connection.netty.BedrockBatchWrapper
import cn.nukkit.network.protocol.types.*
import com.google.common.base.Preconditions
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec

class CompressionCodec(val strategy: CompressionStrategy, private val prefixed: Boolean) :
    MessageToMessageCodec<BedrockBatchWrapper, BedrockBatchWrapper>() {
    @Throws(Exception::class)
    override fun encode(ctx: ChannelHandlerContext, msg: BedrockBatchWrapper, out: MutableList<Any>) {
        check(!(msg.compressed == null && msg.uncompressed == null)) { "Batch was not encoded before" }

        if (msg.compressed != null && !msg.isModified) {
            this.onPassedThrough(ctx, msg)
            out.add(msg.retain())
            return
        }

        val compression = strategy.getCompression(msg)
        check(!(!this.prefixed && strategy.defaultCompression.algorithm !== compression.algorithm)) { "Non-default compression algorithm used without prefixing" }

        val compressed = compression!!.encode(ctx, msg.uncompressed)
        try {
            val outBuf: ByteBuf
            if (this.prefixed) {
                // Do not use a composite buffer as encryption does not like it
                outBuf = ctx.alloc().ioBuffer(1 + compressed!!.readableBytes())
                outBuf.writeByte(getCompressionHeader(compression.algorithm).toInt())
                outBuf.writeBytes(compressed)
            } else {
                outBuf = compressed!!.retain()
            }

            msg.setCompressed(outBuf, compression.algorithm)
        } finally {
            compressed!!.release()
        }

        this.onCompressed(ctx, msg)
        out.add(msg.retain())
    }

    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, msg: BedrockBatchWrapper, out: MutableList<Any>) {
        val compressed = msg.compressed.slice()
        Preconditions.checkArgument(
            compressed.capacity() <= Server.getInstance().settings.networkSettings().maxDecompressSize(),
            "Compressed data size is too big: %s",
            compressed.capacity()
        )

        val compression: BatchCompression?
        if (this.prefixed) {
            val algorithm = this.getCompressionAlgorithm(compressed.readByte())
            compression = strategy.getCompression(algorithm)
        } else {
            compression = strategy.defaultCompression
        }

        msg.algorithm = compression.algorithm

        msg.uncompressed = compression!!.decode(ctx, compressed.slice())
        this.onDecompressed(ctx, msg)
        out.add(msg.retain())
    }

    protected fun onPassedThrough(ctx: ChannelHandlerContext?, msg: BedrockBatchWrapper?) {
    }

    protected fun onCompressed(ctx: ChannelHandlerContext?, msg: BedrockBatchWrapper?) {
    }

    protected fun onDecompressed(ctx: ChannelHandlerContext?, msg: BedrockBatchWrapper?) {
    }

    protected fun getCompressionHeader(algorithm: CompressionAlgorithm): Byte {
        if (algorithm == PacketCompressionAlgorithm.NONE) {
            return 0xff.toByte()
        } else if (algorithm == PacketCompressionAlgorithm.ZLIB) {
            return 0x00
        } else if (algorithm == PacketCompressionAlgorithm.SNAPPY) {
            return 0x01
        }

        val header = this.getCompressionHeader0(algorithm)
        require(header.toInt() != -1) { "Unknown compression algorithm $algorithm" }
        return header
    }

    protected fun getCompressionAlgorithm(header: Byte): CompressionAlgorithm {
        when (header) {
            0x00 -> return PacketCompressionAlgorithm.ZLIB
            0x01 -> return PacketCompressionAlgorithm.SNAPPY
            0xff.toByte() -> return PacketCompressionAlgorithm.NONE
        }

        val algorithm = this.getCompressionAlgorithm0(header)
        requireNotNull(algorithm) { "Unknown compression algorithm $header" }
        return algorithm
    }

    protected fun getCompressionHeader0(algorithm: CompressionAlgorithm?): Byte {
        return -1
    }

    protected fun getCompressionAlgorithm0(header: Byte): CompressionAlgorithm? {
        return null
    }

    override fun toString(): String {
        return strategy.toString()
    }

    companion object {
        const val NAME: String = "compression-codec"
    }
}
