package org.chorus_oss.chorus.network.protocol.types.biome

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

data class BiomeLegacyWorldGenRulesData(
    val legacyPreHills: MutableList<BiomeConditionalTransformationData>
) {
    fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeArray(legacyPreHills) { buf, data ->
            data.encode(buf)
        }
    }
}
