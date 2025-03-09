package org.chorus.network.connection.netty.codec.batch

import org.chorus.network.connection.netty.BedrockBatchWrapper
import org.chorus.network.connection.netty.BedrockPacketWrapper
import org.chorus.utils.ByteBufVarInt
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOutboundHandlerAdapter
import io.netty.channel.ChannelPromise
import java.util.*

class BedrockBatchEncoder : ChannelOutboundHandlerAdapter() {
    private val messages: Queue<BedrockPacketWrapper> = ArrayDeque()

    @Throws(Exception::class)
    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
        if (msg !is BedrockPacketWrapper) {
            super.write(ctx, msg, promise)
            return
        }

        // Accumulate messages to batch
        messages.add(msg)
        promise.trySuccess() // complete write promise here
    }

    @Throws(Exception::class)
    override fun flush(ctx: ChannelHandlerContext) {
        if (messages.isEmpty()) {
            super.flush(ctx)
            return
        }

        val buf = ctx.alloc().compositeDirectBuffer(messages.size * 2)
        val batch: BedrockBatchWrapper = newInstance()

        try {
            var packet: BedrockPacketWrapper
            while ((messages.poll().also { packet = it }) != null) try {
                val message = packet.packetBuffer
                requireNotNull(message) { "BedrockPacket is not encoded" }

                val header = ctx.alloc().ioBuffer(5)
                ByteBufVarInt.writeUnsignedInt(header, message.readableBytes())
                buf.addComponent(true, header)
                buf.addComponent(true, message.retain())
                batch.addPacket(packet.retain())
            } finally {
                packet.release()
            }

            batch.uncompressed = buf.retain()
            ctx.write(batch.retain())
        } finally {
            buf.release()
            batch.release()
        }

        super.flush(ctx)
    }

    @Throws(Exception::class)
    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        var message: BedrockPacketWrapper
        while ((messages.poll().also { message = it }) != null) {
            message.release()
        }
        super.handlerRemoved(ctx)
    }

    companion object {
        const val NAME: String = "bedrock-batch-encoder"
    }
}
