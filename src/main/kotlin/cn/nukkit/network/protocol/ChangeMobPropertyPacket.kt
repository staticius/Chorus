package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

/**
 * Server-bound packet to change the properties of a mob.
 *
 * @since v503
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class ChangeMobPropertyPacket : DataPacket() {
    var uniqueEntityId: Long = 0
    var property: String? = null
    var boolValue: Boolean = false
    var stringValue: String? = null
    var intValue: Int = 0
    var floatValue: Float = 0f

    override fun decode(byteBuf: HandleByteBuf) {
        this.uniqueEntityId = byteBuf.readLong()
        this.property = byteBuf.readString()
        this.boolValue = byteBuf.readBoolean()
        this.stringValue = byteBuf.readString()
        this.intValue = byteBuf.readVarInt()
        this.floatValue = byteBuf.readFloatLE()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeLong(this.uniqueEntityId)
        byteBuf.writeString(property!!)
        byteBuf.writeBoolean(this.boolValue)
        byteBuf.writeString(stringValue!!)
        byteBuf.writeVarInt(this.intValue)
        byteBuf.writeFloatLE(this.floatValue)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CHANGE_MOB_PROPERTY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
