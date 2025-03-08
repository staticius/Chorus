package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.network.protocol.types.EntityLink
import lombok.*

/**
 * @since 15-10-22
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class SetEntityLinkPacket : DataPacket() {
    var vehicleUniqueId: Long = 0 //from
    var riderUniqueId: Long = 0 //to
    var type: EntityLink.Type? = null
    var immediate: Byte = 0
    var riderInitiated: Boolean = false
    var vehicleAngularVelocity: Float = 0f

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityUniqueId(this.vehicleUniqueId)
        byteBuf.writeEntityUniqueId(this.riderUniqueId)
        byteBuf.writeByte(type!!.ordinal.toByte().toInt())
        byteBuf.writeByte(immediate.toInt())
        byteBuf.writeBoolean(this.riderInitiated)
        byteBuf.writeFloatLE(this.vehicleAngularVelocity)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_ENTITY_LINK_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
