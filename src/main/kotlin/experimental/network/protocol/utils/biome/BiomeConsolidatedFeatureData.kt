package org.chorus_oss.chorus.experimental.network.protocol.utils.biome

import org.chorus_oss.chorus.nbt.tag.CompoundTag
import org.chorus_oss.protocol.types.biome.BiomeConsolidatedFeatureData
import org.chorus_oss.protocol.types.biome.BiomeScatterParamData

fun BiomeConsolidatedFeatureData.Companion.fromNBT(nbt: CompoundTag): BiomeConsolidatedFeatureData {
    return BiomeConsolidatedFeatureData(
        scatter = BiomeScatterParamData.fromNBT(nbt.getCompound("scatter")),
        feature = nbt.getShort("feature"),
        identifier = nbt.getShort("identifier"),
        pass = nbt.getShort("pass"),
        canUseInternal = nbt.getBoolean("canUseInternalFeature"),
    )
}