package org.chorus.network.protocol

import cn.nukkit.entity.data.EntityDataMap
import cn.nukkit.item.Item
import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.utils.*
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityUniqueId(this.entityUniqueId)
        byteBuf.writeEntityRuntimeId(this.entityRuntimeId)
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
