package org.chorus.network.protocol

import cn.nukkit.math.*
import cn.nukkit.nbt.NBTIO.readNetworkCompressed
import cn.nukkit.nbt.NBTIO.writeNetwork
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.IntTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.nbt.tag.Tag
import cn.nukkit.network.connection.util.HandleByteBuf
import io.netty.buffer.ByteBufInputStream
import io.netty.handler.codec.EncoderException
import lombok.*
import java.io.IOException
import java.lang.String

/**
 * @author joserobjr
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class PositionTrackingDBServerBroadcastPacket : DataPacket() {
    private var action: Action? = null
    private var trackingId = 0
    private var tag: CompoundTag? = null

    override fun decode(byteBuf: HandleByteBuf) {
        action = ACTIONS[byteBuf.readByte().toInt()]
        trackingId = byteBuf.readVarInt()
        try {
            ByteBufInputStream(byteBuf).use { inputStream ->
                tag = readNetworkCompressed(inputStream)
            }
        } catch (e: IOException) {
            throw EncoderException(e)
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(action!!.ordinal().toByte().toInt())
        byteBuf.writeVarInt(trackingId)
        try {
            byteBuf.writeBytes(writeNetwork((if (tag != null) tag else CompoundTag())!!)!!)
        } catch (e: IOException) {
            throw EncoderException(e)
        }
    }

    private fun requireTag(): CompoundTag? {
        if (tag == null) {
            tag = CompoundTag()
                .putByte("version", 1)
                .putString("id", String.format("0x%08x", trackingId))
        }
        return tag
    }

    fun setAction(action: Action?) {
        this.action = action
    }

    fun setTrackingId(trackingId: Int) {
        this.trackingId = trackingId
        if (tag != null) {
            tag!!.putString("id", String.format("0x%08x", trackingId))
        }
    }

    var position: BlockVector3?
        get() {
            if (tag == null) {
                return null
            }
            val pos = tag!!.getList("pos", IntTag::class.java)
            if (pos == null || pos.size() != 3) {
                return null
            }
            return BlockVector3(pos.get(0)!!.data, pos.get(1)!!.data, pos.get(2)!!.data)
        }
        set(position) {
            setPosition(position!!.x, position.y, position.z)
        }

    fun setPosition(position: Vector3) {
        setPosition(position.floorX, position.floorY, position.floorZ)
    }

    fun setPosition(x: Int, y: Int, z: Int) {
        requireTag()!!.putList(
            "pos", ListTag<Tag?>()
                .add(IntTag(x))
                .add(IntTag(y))
                .add(IntTag(z))
        )
    }

    var status: Int
        get() {
            if (tag == null) {
                return 0
            }
            return tag!!.getByte("status").toInt()
        }
        set(status) {
            requireTag()!!.putByte("status", status)
        }

    var version: Int
        get() {
            if (tag == null) {
                return 0
            }
            return tag!!.getByte("version").toInt()
        }
        set(status) {
            requireTag()!!.putByte("version", status)
        }

    var dimension: Int
        get() {
            if (tag == null) {
                return 0
            }
            return tag!!.getByte("dim").toInt()
        }
        set(dimension) {
            requireTag()!!.putInt("dim", dimension)
        }

    enum class Action {
        UPDATE,
        DESTROY,
        NOT_FOUND
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.POS_TRACKING_SERVER_BROADCAST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        private val ACTIONS = Action.entries.toTypedArray()
    }
}
