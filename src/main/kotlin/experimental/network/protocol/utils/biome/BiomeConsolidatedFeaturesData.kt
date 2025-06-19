package org.chorus_oss.chorus.experimental.network.protocol.utils.biome

import org.chorus_oss.chorus.nbt.tag.CompoundTag
import org.chorus_oss.protocol.types.biome.BiomeConsolidatedFeatureData
import org.chorus_oss.protocol.types.biome.BiomeConsolidatedFeaturesData

fun BiomeConsolidatedFeaturesData.Companion.fromNBT(nbt: CompoundTag): BiomeConsolidatedFeaturesData {
    return BiomeConsolidatedFeaturesData(
        features = nbt.getList("features", CompoundTag::class.java).all.map { BiomeConsolidatedFeatureData.fromNBT(it) }
    )
}