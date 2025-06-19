package org.chorus_oss.chorus.experimental.network.protocol.utils.biome

import org.chorus_oss.chorus.nbt.tag.CompoundTag
import org.chorus_oss.protocol.types.biome.BiomeConditionalTransformationData
import org.chorus_oss.protocol.types.biome.BiomeOverworldGenRulesData
import org.chorus_oss.protocol.types.biome.BiomeWeightedData
import org.chorus_oss.protocol.types.biome.BiomeWeightedTemperatureData

fun BiomeOverworldGenRulesData.Companion.fromNBT(nbt: CompoundTag): BiomeOverworldGenRulesData {
    return BiomeOverworldGenRulesData(
        hillsTransformations = nbt.getList("hillsTransformations", CompoundTag::class.java).all.map { BiomeWeightedData.fromNBT(it) },
        mutateTransformations = nbt.getList("mutateTransformations", CompoundTag::class.java).all.map { BiomeWeightedData.fromNBT(it) },
        riverTransformations = nbt.getList("riverTransformations", CompoundTag::class.java).all.map { BiomeWeightedData.fromNBT(it) },
        shoreTransformations = nbt.getList("shoreTransformations", CompoundTag::class.java).all.map { BiomeWeightedData.fromNBT(it) },
        preHillsEdge = nbt.getList("preHillsEdge", CompoundTag::class.java).all.map { BiomeConditionalTransformationData.fromNBT(it) },
        postShoreEdge = nbt.getList("postShoreEdge", CompoundTag::class.java).all.map { BiomeConditionalTransformationData.fromNBT(it) },
        climate = nbt.getList("climate", CompoundTag::class.java).all.map { BiomeWeightedTemperatureData.fromNBT(it) },
    )
}