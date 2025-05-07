package org.chorus_oss.chorus.network.protocol.types.biome

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

data class BiomeCappedSurfaceData(
    val floorBlocks: MutableList<Int>,
    val ceilingBlocks: MutableList<Int>,
    val seaBlock: UInt? = null,
    val foundationBlock: UInt? = null,
    val beachBlock: UInt? = null,
) {
    fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeArray(floorBlocks) { buf, data ->
            buf.writeIntLE(data)
        }
        byteBuf.writeArray(ceilingBlocks) { buf, data ->
            buf.writeIntLE(data)
        }
        byteBuf.writeNotNull(seaBlock) { byteBuf.writeIntLE(it.toInt()) }
        byteBuf.writeNotNull(foundationBlock) { byteBuf.writeIntLE(it.toInt()) }
        byteBuf.writeNotNull(beachBlock) { byteBuf.writeIntLE(it.toInt()) }
    }
}
