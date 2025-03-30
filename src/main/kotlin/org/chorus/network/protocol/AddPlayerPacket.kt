package org.chorus.network.protocol

import org.chorus.entity.data.EntityDataMap
import org.chorus.item.Item
import org.chorus.math.Vector2f
import org.chorus.math.Vector3f
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.*
import org.chorus.utils.Binary
import java.util.*

data class AddPlayerPacket(
    val uuid: UUID,
    val playerName: String,
    val targetRuntimeID: ActorRuntimeID,
    val platformChatID: String,
    val position: Vector3f,
    val velocity: Vector3f,
    val rotation: Vector2f,
    val yHeadRotation: Float,
    val carriedItem: Item,
    val playerGameType: Int,
    val entityDataMap: EntityDataMap,
    val syncedProperties: PropertySyncData,
    val abilitiesData: SerializedAbilitiesData,
    val actorLinks: Array<EntityLink>,
    val deviceID: String,
    val buildPlatform: Platform,
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUUID(this.uuid)
        byteBuf.writeString(this.playerName)
        byteBuf.writeActorRuntimeID(this.targetRuntimeID)
        byteBuf.writeString(this.platformChatID)
        byteBuf.writeVector3f(this.position)
        byteBuf.writeVector3f(this.velocity)
        byteBuf.writeVector2f(this.rotation)
        byteBuf.writeFloatLE(this.yHeadRotation)
        byteBuf.writeSlot(this.carriedItem)
        byteBuf.writeVarInt(this.playerGameType)
        byteBuf.writeBytes(Binary.writeEntityData(this.entityDataMap))
        byteBuf.writePropertySyncData(this.syncedProperties)
        this.abilitiesData.write(byteBuf)
        byteBuf.writeArray(this.actorLinks) {
            byteBuf.writeEntityLink(it)
        }
        byteBuf.writeString(this.deviceID)
        byteBuf.writeIntLE(this.buildPlatform.id)
    }

    override fun pid(): Int {
        return ProtocolInfo.ADD_PLAYER_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AddPlayerPacket

        if (targetRuntimeID != other.targetRuntimeID) return false
        if (yHeadRotation != other.yHeadRotation) return false
        if (playerGameType != other.playerGameType) return false
        if (uuid != other.uuid) return false
        if (playerName != other.playerName) return false
        if (platformChatID != other.platformChatID) return false
        if (position != other.position) return false
        if (velocity != other.velocity) return false
        if (rotation != other.rotation) return false
        if (carriedItem != other.carriedItem) return false
        if (entityDataMap != other.entityDataMap) return false
        if (syncedProperties != other.syncedProperties) return false
        if (abilitiesData != other.abilitiesData) return false
        if (!actorLinks.contentEquals(other.actorLinks)) return false
        if (deviceID != other.deviceID) return false
        if (buildPlatform != other.buildPlatform) return false

        return true
    }

    override fun hashCode(): Int {
        var result = targetRuntimeID.hashCode()
        result = 31 * result + yHeadRotation.hashCode()
        result = 31 * result + playerGameType
        result = 31 * result + uuid.hashCode()
        result = 31 * result + playerName.hashCode()
        result = 31 * result + platformChatID.hashCode()
        result = 31 * result + position.hashCode()
        result = 31 * result + velocity.hashCode()
        result = 31 * result + rotation.hashCode()
        result = 31 * result + carriedItem.hashCode()
        result = 31 * result + entityDataMap.hashCode()
        result = 31 * result + syncedProperties.hashCode()
        result = 31 * result + abilitiesData.hashCode()
        result = 31 * result + actorLinks.contentHashCode()
        result = 31 * result + deviceID.hashCode()
        result = 31 * result + buildPlatform.hashCode()
        return result
    }
}
