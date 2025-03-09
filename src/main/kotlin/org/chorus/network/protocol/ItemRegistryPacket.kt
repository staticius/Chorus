package org.chorus.network.protocol

import cn.nukkit.nbt.NBTIO.write
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.utils.MainLogger
import lombok.*
import java.io.IOException
import java.nio.ByteOrder

/**
 * @author GoodLucky777
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class ItemRegistryPacket : DataPacket() {
    private var entries: Array<Entry?>? = Entry.EMPTY_ARRAY

    override fun decode(byteBuf: HandleByteBuf) {}

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(entries!!.size)
        try {
            for (entry in entries!!) {
                byteBuf.writeString(entry.name)
                byteBuf.writeShortLE(entry.runtimeId)
                byteBuf.writeBoolean(entry.componentBased)
                byteBuf.writeVarInt(entry.version)
                byteBuf.writeBytes(write(entry.data, ByteOrder.LITTLE_ENDIAN, true)!!)
            }
        } catch (e: IOException) {
            MainLogger.logger.error("Error while encoding NBT data of ItemRegistryPacket", e)
        }
    }

    fun setEntries(entries: Array<Entry?>?) {
        this.entries = if (entries == null) null else if (entries.size == 0) Entry.EMPTY_ARRAY else entries.clone()
    }

    fun getEntries(): Array<Entry>? {
        return if (entries == null) null else if (entries!!.size == 0) Entry.EMPTY_ARRAY else entries!!.clone()
    }

    @ToString
    class Entry(
        val name: String,
        val runtimeId: Int, val version: Int,
        val componentBased: Boolean,
        val data: CompoundTag
    ) {
        companion object {
            val EMPTY_ARRAY: Array<Entry?> = arrayOfNulls(0)
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.ITEM_REGISTRY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
