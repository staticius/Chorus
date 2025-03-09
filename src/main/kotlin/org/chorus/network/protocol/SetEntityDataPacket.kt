package org.chorus.network.protocol

import org.chorus.entity.data.EntityDataMap
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.PropertySyncData
import org.chorus.utils.*
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class SetEntityDataPacket : DataPacket() {
    @JvmField
    var eid: Long = 0
    @JvmField
    var entityData: EntityDataMap? = null
    var syncedProperties: PropertySyncData = PropertySyncData(intArrayOf(), floatArrayOf())
    var frame: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarLong(this.eid)
        byteBuf.writeBytes(Binary.writeEntityData(this.entityData))
        byteBuf.writePropertySyncData(syncedProperties)
        byteBuf.writeUnsignedVarLong(this.frame)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_ENTITY_DATA_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
