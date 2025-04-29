package org.chorus_oss.chorus.network.protocol.types

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

data class MaterialReducerDataEntry(
    val fromItemKeyInput: Int,
    val itemIDsAndCounts: List<Item>
) {
    fun write(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(fromItemKeyInput)
        byteBuf.writeArray(itemIDsAndCounts) { buf, item ->
            buf.writeVarInt(item.itemID)
            buf.writeVarInt(item.itemCount)
        }
    }

    data class Item(
        val itemID: Int,
        val itemCount: Int,
    )
}