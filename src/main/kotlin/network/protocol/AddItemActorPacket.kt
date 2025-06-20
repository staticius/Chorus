package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.entity.data.EntityDataMap
import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.math.Vector3f
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.network.protocol.types.ActorRuntimeID
import org.chorus_oss.chorus.network.protocol.types.ActorUniqueID
import org.chorus_oss.chorus.utils.Binary

data class AddItemActorPacket(
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
        return ProtocolInfo.ADD_ITEM_ACTOR_PACKET
    }
}
