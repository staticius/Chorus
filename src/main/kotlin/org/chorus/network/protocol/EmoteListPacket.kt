package org.chorus.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import lombok.*
import java.util.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class EmoteListPacket : DataPacket() {
    var runtimeId: Long = 0
    val pieceIds: MutableList<UUID> = ObjectArrayList()

    override fun decode(byteBuf: HandleByteBuf) {
        this.runtimeId = byteBuf.readEntityRuntimeId()
        for (i in 0..<byteBuf.readUnsignedVarInt()) {
            val id = byteBuf.readUUID()
            pieceIds.add(id)
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityRuntimeId(runtimeId)
        byteBuf.writeUnsignedVarInt(pieceIds.size)
        for (id in pieceIds) {
            byteBuf.writeUUID(id)
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.EMOTE_LIST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
