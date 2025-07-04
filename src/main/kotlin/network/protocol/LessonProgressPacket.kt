package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.network.protocol.types.LessonAction


class LessonProgressPacket : DataPacket() {
    var action: LessonAction? = null
    var score: Int = 0
    var activityId: String? = null

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(action!!.ordinal)
        byteBuf.writeVarInt(score)
        byteBuf.writeString(activityId!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.LESSON_PROGRESS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<LessonProgressPacket> {
        override fun decode(byteBuf: HandleByteBuf): LessonProgressPacket {
            val packet = LessonProgressPacket()

            packet.action = LessonAction.entries[byteBuf.readVarInt()]
            packet.score = byteBuf.readVarInt()
            packet.activityId = byteBuf.readString()

            return packet
        }
    }
}
