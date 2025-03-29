package org.chorus.network.protocol

import org.chorus.entity.Attribute
import org.chorus.entity.data.EntityDataMap
import org.chorus.math.Vector2f
import org.chorus.math.Vector3f
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.ActorRuntimeID
import org.chorus.network.protocol.types.ActorUniqueID
import org.chorus.network.protocol.types.EntityLink
import org.chorus.network.protocol.types.PropertySyncData
import org.chorus.utils.Binary

data class AddEntityPacket(
    val targetActorID: ActorUniqueID,
    val targetRuntimeID: ActorRuntimeID,
    val actorType: String,
    val position: Vector3f,
    val velocity: Vector3f,
    val rotation: Vector2f,
    val yHeadRotation: Float,
    val yBodyRotation: Float,
    val attributeList: Array<Attribute>,
    val actorData: EntityDataMap,
    val syncedProperties: PropertySyncData,
    val actorLinks: Array<EntityLink>
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeActorUniqueID(this.targetActorID)
        byteBuf.writeActorRuntimeID(this.targetRuntimeID)
        byteBuf.writeString(this.actorType)
        byteBuf.writeVector3f(this.position)
        byteBuf.writeVector3f(this.velocity)
        byteBuf.writeVector2f(this.rotation)
        byteBuf.writeFloatLE(this.yHeadRotation)
        byteBuf.writeFloatLE(this.yBodyRotation)
        byteBuf.writeAttributeList(this.attributeList)
        byteBuf.writeBytes(Binary.writeEntityData(this.actorData))
        byteBuf.writePropertySyncData(this.syncedProperties)
        byteBuf.writeArray(this.actorLinks) { byteBuf.writeEntityLink(it) }
    }

    override fun pid(): Int {
        return ProtocolInfo.ADD_ENTITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AddEntityPacket

        if (targetActorID != other.targetActorID) return false
        if (targetRuntimeID != other.targetRuntimeID) return false
        if (yHeadRotation != other.yHeadRotation) return false
        if (yBodyRotation != other.yBodyRotation) return false
        if (actorType != other.actorType) return false
        if (position != other.position) return false
        if (velocity != other.velocity) return false
        if (rotation != other.rotation) return false
        if (!attributeList.contentEquals(other.attributeList)) return false
        if (actorData != other.actorData) return false
        if (syncedProperties != other.syncedProperties) return false
        if (!actorLinks.contentEquals(other.actorLinks)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = targetActorID.hashCode()
        result = 31 * result + targetRuntimeID.hashCode()
        result = 31 * result + yHeadRotation.hashCode()
        result = 31 * result + yBodyRotation.hashCode()
        result = 31 * result + actorType.hashCode()
        result = 31 * result + position.hashCode()
        result = 31 * result + velocity.hashCode()
        result = 31 * result + rotation.hashCode()
        result = 31 * result + attributeList.contentHashCode()
        result = 31 * result + actorData.hashCode()
        result = 31 * result + syncedProperties.hashCode()
        result = 31 * result + actorLinks.contentHashCode()
        return result
    }
}
