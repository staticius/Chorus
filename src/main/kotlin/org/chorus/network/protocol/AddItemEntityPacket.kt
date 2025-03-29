package org.chorus.network.protocol

import org.chorus.entity.data.EntityDataMap
import org.chorus.item.Item
import org.chorus.math.Vector3f
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.ActorRuntimeID
import org.chorus.network.protocol.types.ActorUniqueID
import org.chorus.utils.Binary

data class AddItemEntityPacket(
    val targetActorID: ActorUniqueID,
    val targetRuntimeID: ActorRuntimeID,
    val item: Item,
    val position: Vector3f,
    val velocity: Vector3f,
    val entityData: EntityDataMap,
    val fromFishing: Boolean,
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeActorUniqueID(this.targetActorID)
        byteBuf.writeActorRuntimeID(this.targetRuntimeID)
        byteBuf.writeSlot(this.item)
        byteBuf.writeVector3f(this.position)
        byteBuf.writeVector3f(this.velocity)
        byteBuf.writeBytes(Binary.writeEntityData(this.entityData))
        byteBuf.writeBoolean(this.fromFishing)
    }

    override fun pid(): Int {
        return ProtocolInfo.ADD_ITEM_ENTITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
