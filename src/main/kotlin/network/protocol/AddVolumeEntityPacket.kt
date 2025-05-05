package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.math.BlockVector3
import org.chorus_oss.chorus.nbt.tag.CompoundTag
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

data class AddVolumeEntityPacket(
    val entityNetworkID: Int,
    val components: CompoundTag,
    val jsonIdentifier: String,
    val instanceName: String,
    val minBounds: BlockVector3,
    val maxBounds: BlockVector3,
    val dimensionType: Int,
    val engineVersion: String,
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(entityNetworkID)
        byteBuf.writeTag(components)
        byteBuf.writeString(jsonIdentifier)
        byteBuf.writeString(instanceName)
        byteBuf.writeBlockVector3(minBounds)
        byteBuf.writeBlockVector3(maxBounds)
        byteBuf.writeVarInt(dimensionType)
        byteBuf.writeString(engineVersion)
    }

    override fun pid(): Int {
        return ProtocolInfo.ADD_VOLUME_ENTITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
