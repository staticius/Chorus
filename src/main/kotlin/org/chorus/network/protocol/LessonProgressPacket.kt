package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.LessonAction







class LessonProgressPacket : DataPacket() {
    var action: LessonAction? = null
    var score: Int = 0
    var activityId: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
        this.action = LessonAction.entries[byteBuf.readVarInt()]
        this.score = byteBuf.readVarInt()
        this.activityId = byteBuf.readString()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(action!!.ordinal)
        byteBuf.writeVarInt(score)
        byteBuf.writeString(activityId!!)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.LESSON_PROGRESS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
