package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.utils.UUIDValidator

import java.util.*


class ResourcePackClientResponsePacket : DataPacket() {
    var responseStatus: Byte = 0
    var packEntries: Array<Entry>

    override fun decode(byteBuf: HandleByteBuf) {
        this.responseStatus = byteBuf.readByte()
        this.packEntries = arrayOfNulls(byteBuf.readShortLE().toInt())
        for (i in packEntries.indices) {
            val entry: Array<String> = byteBuf.readString().split("_")

            if (UUIDValidator.isValidUUID(entry[0])) {
                // Literally a server crash if spammed.
                // @Zwuiix
                packEntries[i] = Entry(
                    UUID.fromString(
                        entry[0]
                    ), entry[1]
                )
            }
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(responseStatus.toInt())
        byteBuf.writeShortLE(packEntries.size)
        for (entry in this.packEntries) {
            byteBuf.writeString(entry.uuid.toString() + '_' + entry.version)
        }
    }


    class Entry(val uuid: UUID, val version: String)

    override fun pid(): Int {
        return ProtocolInfo.Companion.RESOURCE_PACK_CLIENT_RESPONSE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        const val STATUS_REFUSED: Byte = 1
        const val STATUS_SEND_PACKS: Byte = 2
        const val STATUS_HAVE_ALL_PACKS: Byte = 3
        const val STATUS_COMPLETED: Byte = 4
    }
}
