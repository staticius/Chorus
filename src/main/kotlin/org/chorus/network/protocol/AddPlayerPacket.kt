package org.chorus.network.protocol

import org.chorus.Server
import org.chorus.entity.data.EntityDataMap
import org.chorus.item.Item
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.PropertySyncData
import org.chorus.utils.*
import lombok.*
import java.util.*






class AddPlayerPacket : DataPacket() {
    var uuid: UUID? = null
    var username: String? = null
    var entityUniqueId: Long = 0
    var entityRuntimeId: Long = 0
    var platformChatId: String = ""
    var x: Float = 0f
    var y: Float = 0f
    var z: Float = 0f
    var speedX: Float = 0f
    var speedY: Float = 0f
    var speedZ: Float = 0f
    var pitch: Float = 0f
    var yaw: Float = 0f
    var item: Item? = null
    var gameType: Int = Server.getInstance().gamemode
    var entityData: EntityDataMap = EntityDataMap()
    var syncedProperties: PropertySyncData = PropertySyncData(intArrayOf(), floatArrayOf())
    var deviceId: String = ""
    var buildPlatform: Int = -1

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUUID(uuid!!)
        byteBuf.writeString(username!!)
        byteBuf.writeEntityRuntimeId(this.entityRuntimeId)
        byteBuf.writeString(this.platformChatId)
        byteBuf.writeVector3f(this.x, this.y, this.z)
        byteBuf.writeVector3f(this.speedX, this.speedY, this.speedZ)
        byteBuf.writeFloatLE(this.pitch)
        byteBuf.writeFloatLE(this.yaw)
        byteBuf.writeFloatLE(this.yaw)
        byteBuf.writeSlot(this.item)
        byteBuf.writeVarInt(this.gameType)
        byteBuf.writeBytes(Binary.writeEntityData(this.entityData))
        byteBuf.writePropertySyncData(syncedProperties)
        //        byteBuf.writeUnsignedVarInt(0); //TODO: Adventure settings
//        byteBuf.writeUnsignedVarInt(0);
//        byteBuf.writeUnsignedVarInt(0);
//        byteBuf.writeUnsignedVarInt(0);
//        byteBuf.writeUnsignedVarInt(0);
        byteBuf.writeLongLE(entityUniqueId)
        byteBuf.writeUnsignedVarInt(0) // playerPermission
        byteBuf.writeUnsignedVarInt(0) // commandPermission
        byteBuf.writeUnsignedVarInt(1) // abilitiesLayer size
        byteBuf.writeShortLE(1) // BASE layer type
        byteBuf.writeIntLE(262143) // abilitiesSet - all abilities
        byteBuf.writeIntLE(63) // abilityValues - survival abilities
        byteBuf.writeFloatLE(0.1f) // flySpeed
        byteBuf.writeFloatLE(0.1f) // vertical fly speed
        byteBuf.writeFloatLE(0.05f) // walkSpeed
        byteBuf.writeUnsignedVarInt(0) //TODO: Entity links
        byteBuf.writeString(deviceId)
        byteBuf.writeIntLE(buildPlatform)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.ADD_PLAYER_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
