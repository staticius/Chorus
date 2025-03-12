package org.chorus.network.protocol

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.chorus.network.connection.util.HandleByteBuf
import java.util.*


class SetScoreboardIdentityPacket : DataPacket() {
    val entries: List<Entry> = ObjectArrayList()
    var action: Action? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(action!!.ordinal().toByte().toInt())

        for (entry in this.entries) {
            byteBuf.writeVarLong(entry.scoreboardId)
            byteBuf.writeUUID(entry.uuid!!)
        }
    }

    enum class Action {
        ADD,
        REMOVE
    }

    class Entry {
        var scoreboardId: Long = 0
        var uuid: UUID? = null
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_SCOREBOARD_IDENTITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
