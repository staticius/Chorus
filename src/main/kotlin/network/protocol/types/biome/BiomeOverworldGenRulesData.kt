package org.chorus_oss.chorus.network.protocol.types.biome

import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

data class BiomeOverworldGenRulesData(
    val hillsTransformations: MutableList<BiomeWeightedData>,
    val mutateTransformations: MutableList<BiomeWeightedData>,
    val riverTransformations: MutableList<BiomeWeightedData>,
    val shoreTransformations: MutableList<BiomeWeightedData>,
    val preHillsEdge: MutableList<BiomeConditionalTransformationData>,
    val postShoreEdge: MutableList<BiomeConditionalTransformationData>,
    val climate: MutableList<BiomeWeightedTemperatureData>,
) {
    fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeArray(hillsTransformations) { buf, data ->
            data.encode(buf)
        }
        byteBuf.writeArray(mutateTransformations) { buf, data ->
            data.encode(buf)
        }
        byteBuf.writeArray(riverTransformations) { buf, data ->
            data.encode(buf)
        }
        byteBuf.writeArray(shoreTransformations) { buf, data ->
            data.encode(buf)
        }
        byteBuf.writeArray(preHillsEdge) { buf, data ->
            data.encode(buf)
        }
        byteBuf.writeArray(postShoreEdge) { buf, data ->
            data.encode(buf)
        }
        byteBuf.writeArray(climate) { buf, data ->
            data.encode(buf)
        }
    }
}
