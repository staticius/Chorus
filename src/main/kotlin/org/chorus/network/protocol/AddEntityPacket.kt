package org.chorus.network.protocol

import org.chorus.entity.Attribute
import org.chorus.entity.data.EntityDataMap
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.EntityLink
import org.chorus.network.protocol.types.PropertySyncData
import org.chorus.registry.Registries
import org.chorus.utils.*







class AddEntityPacket : DataPacket() {
    @JvmField
    var entityUniqueId: Long = 0
    @JvmField
    var entityRuntimeId: Long = 0
    @JvmField
    var type: Int = 0
    var id: String? = null
    @JvmField
    var x: Float = 0f
    @JvmField
    var y: Float = 0f
    @JvmField
    var z: Float = 0f
    @JvmField
    var speedX: Float = 0f
    @JvmField
    var speedY: Float = 0f
    @JvmField
    var speedZ: Float = 0f
    var yaw: Float = 0f
    var pitch: Float = 0f
    var headYaw: Float = 0f

    //todo: check what's the usage of this
    var bodyYaw: Float = -1f
    var attributes: Array<Attribute?> = Attribute.EMPTY_ARRAY
    @JvmField
    var entityData: EntityDataMap = EntityDataMap()
    var syncedProperties: PropertySyncData = PropertySyncData(intArrayOf(), floatArrayOf())
    var links: Array<EntityLink?> = EntityLink.Companion.EMPTY_ARRAY

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeEntityUniqueId(this.entityUniqueId)
        byteBuf.writeEntityRuntimeId(this.entityRuntimeId)
        if (id == null) {
            id = Registries.ENTITY.getEntityIdentifier(type)
        }
        byteBuf.writeString(id!!)
        byteBuf.writeVector3f(this.x, this.y, this.z)
        byteBuf.writeVector3f(this.speedX, this.speedY, this.speedZ)
        byteBuf.writeFloatLE(this.pitch)
        byteBuf.writeFloatLE(this.yaw)
        byteBuf.writeFloatLE(this.headYaw)
        byteBuf.writeFloatLE(if (this.bodyYaw != -1f) this.bodyYaw else this.yaw)
        byteBuf.writeAttributeList(this.attributes)
        byteBuf.writeBytes(Binary.writeEntityData(this.entityData))
        byteBuf.writePropertySyncData(syncedProperties)
        byteBuf.writeArray(links) { link: EntityLink? ->
            byteBuf.writeEntityLink(
                link!!
            )
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.ADD_ENTITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
