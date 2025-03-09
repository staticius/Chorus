package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.scoreboard.data.DisplaySlot
import org.chorus.scoreboard.data.SortOrder
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class SetDisplayObjectivePacket : DataPacket() {
    @JvmField
    var displaySlot: DisplaySlot? = null
    @JvmField
    var objectiveName: String? = null
    @JvmField
    var displayName: String? = null
    @JvmField
    var criteriaName: String? = null
    @JvmField
    var sortOrder: SortOrder? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(displaySlot!!.slotName)
        byteBuf.writeString(objectiveName!!)
        byteBuf.writeString(displayName!!)
        byteBuf.writeString(criteriaName!!)
        byteBuf.writeVarInt(sortOrder!!.ordinal())
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_DISPLAY_OBJECTIVE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
