package cn.nukkit.network.connection.netty.codec

import cn.nukkit.network.connection.netty.BedrockBatchWrapper
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec
import lombok.extern.slf4j.Slf4j
import org.cloudburstmc.netty.channel.raknet.RakReliability
import org.cloudburstmc.netty.channel.raknet.packet.RakMessage

@Sharable
@Slf4j
class FrameIdCodec(private val frameId: Int) : MessageToMessageCodec<RakMessage, BedrockBatchWrapper>() {
    @Throws(Exception::class)
    override fun encode(ctx: ChannelHandlerContext, msg: BedrockBatchWrapper, out: MutableList<Any>) {
        if (msg.compressed == null) {
            FrameIdCodec.log.error("Bedrock batch was not compressed!")
            throw IllegalStateException("Bedrock batch was not compressed")
        }

        val buf = ctx.alloc().compositeDirectBuffer(2)
        try {
            buf.addComponent(true, ctx.alloc().ioBuffer(1).writeByte(frameId))
            buf.addComponent(true, msg.compressed.retainedSlice())

            out.add(buf.retain())
        } finally {
            buf.release()
        }
    }

    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, msg: RakMessage, out: MutableList<Any>) {
        if (msg.channel() != 0 && msg.reliability() != RakReliability.RELIABLE_ORDERED) {
            return
        }
        val `in` = msg.content()
        if (!`in`.isReadable) {
            return
        }
        val id = `in`.readUnsignedByte().toInt()
        check(id == frameId) { "Invalid frame ID: $id" }
        out.add(BedrockBatchWrapper.Companion.newInstance(`in`.readRetainedSlice(`in`.readableBytes()), null))
    }

    companion object {
        const val NAME: String = "frame-id-codec"
    }
}
