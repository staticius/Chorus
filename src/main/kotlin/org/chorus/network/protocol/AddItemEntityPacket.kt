package org.chorus.network.protocol

import org.chorus.entity.data.EntityDataMap
import org.chorus.item.Item
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.utils.Binary

class AddItemEntityPacket : DataPacket() {
    var entityUniqueId: Long = 0
    var entityRuntimeId: Long = 0
    var item: Item? = null
    var x: Float = 0f
    var y: Float = 0f
    var z: Float = 0f
    var speedX: Float = 0f
    var speedY: Float = 0f
    var speedZ: Float = 0f
    var entityData: EntityDataMap = EntityDataMap()
    var isFromFishing: Boolean = false

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeActorUniqueID(this.entityUniqueId)
        byteBuf.writeActorRuntimeID(this.entityRuntimeId)
        byteBuf.writeSlot(this.item)
        byteBuf.writeVector3f(this.x, this.y, this.z)
        byteBuf.writeVector3f(this.speedX, this.speedY, this.speedZ)
        byteBuf.writeBytes(Binary.writeEntityData(entityData))
        byteBuf.writeBoolean(this.isFromFishing)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.ADD_ITEM_ENTITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
