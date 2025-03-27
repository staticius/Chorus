package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.ActorRuntimeID
import org.chorus.network.protocol.types.ActorUniqueID


class ActorPickRequestPacket(
    val actorID: ActorUniqueID,
    val maxSlots: Byte,
    val withData: Boolean,
) : DataPacket() {
    override fun pid(): Int {
        return ProtocolInfo.ACTOR_PICK_REQUEST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<ActorPickRequestPacket> {
        override fun decode(byteBuf: HandleByteBuf): ActorPickRequestPacket {
            return ActorPickRequestPacket(
                actorID = byteBuf.readActorUniqueID(),
                maxSlots = byteBuf.readByte(),
                withData = byteBuf.readBoolean(),
            )
        }
    }
}
