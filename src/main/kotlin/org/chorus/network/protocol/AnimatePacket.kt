package org.chorus.network.protocol

import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.ActorUniqueID


class AnimatePacket(
    var action: Action,
    var targetUniqueID: ActorUniqueID,
    var rowingTime: Float? = null,
) : DataPacket(), PacketEncoder {
    enum class Action(val id: Int) {
        NO_ACTION(0),
        SWING_ARM(1),
        WAKE_UP(3),
        CRITICAL_HIT(4),
        MAGIC_CRITICAL_HIT(5),
        ROW_RIGHT(128),
        ROW_LEFT(129);

        companion object {
            private val ID_LOOKUP: Int2ObjectMap<Action> = Int2ObjectOpenHashMap()

            init {
                for (value in entries) {
                    ID_LOOKUP.put(value.id, value)
                }
            }

            fun fromId(id: Int): Action {
                return ID_LOOKUP[id]
            }
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(action.id)
        byteBuf.writeActorRuntimeID(this.targetUniqueID)
        if (this.action == Action.ROW_RIGHT || this.action == Action.ROW_LEFT) {
            byteBuf.writeFloatLE(this.rowingTime!!)
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.ANIMATE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<AnimatePacket> {
        override fun decode(byteBuf: HandleByteBuf): AnimatePacket {
            val action = Action.fromId(byteBuf.readVarInt())

            return AnimatePacket(
                action,
                targetUniqueID = byteBuf.readActorRuntimeID(),
                rowingTime = if (action == Action.ROW_RIGHT || action == Action.ROW_LEFT) {
                    byteBuf.readFloatLE()
                } else null
            )
        }
    }
}
