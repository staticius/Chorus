package cn.nukkit.network.connection.netty.codec.batch

import cn.nukkit.network.connection.netty.BedrockBatchWrapper
import cn.nukkit.utils.ByteBufVarInt
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder

@Sharable
class BedrockBatchDecoder : MessageToMessageDecoder<BedrockBatchWrapper>() {
    override fun decode(ctx: ChannelHandlerContext, msg: BedrockBatchWrapper, out: MutableList<Any>) {
        checkNotNull(msg.uncompressed) { "Batch packet was not decompressed" }

        val buffer = msg.uncompressed.slice()
        while (buffer.isReadable) {
            val packetLength = ByteBufVarInt.readUnsignedInt(buffer)
            val packetBuf = buffer.readRetainedSlice(packetLength)
            out.add(packetBuf)
        }
    }

    companion object {
        const val NAME: String = "bedrock-batch-decoder"
    }
}
