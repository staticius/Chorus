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
    val actorLinks: List<EntityLink>,
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
        byteBuf.writeArray(this.actorLinks) { buf, link ->
            buf.writeEntityLink(link)
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
}
