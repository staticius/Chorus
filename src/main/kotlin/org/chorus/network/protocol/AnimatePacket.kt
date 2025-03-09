package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class AnimatePacket : DataPacket() {
    @JvmField
    var eid: Long = 0
    @JvmField
    var action: Action? = null
    var rowingTime: Float = 0f

    override fun decode(byteBuf: HandleByteBuf) {
        this.action = Action.fromId(byteBuf.readVarInt())
        this.eid = byteBuf.readEntityRuntimeId()
        if (this.action == Action.ROW_RIGHT || this.action == Action.ROW_LEFT) {
            this.rowingTime = byteBuf.readFloatLE()
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(action!!.id)
        byteBuf.writeEntityRuntimeId(this.eid)
        if (this.action == Action.ROW_RIGHT || this.action == Action.ROW_LEFT) {
            byteBuf.writeFloatLE(this.rowingTime)
        }
    }

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

    override fun pid(): Int {
        return ProtocolInfo.Companion.ANIMATE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
