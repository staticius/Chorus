package org.chorus_oss.chorus.experimental.network.protocol.utils.biome

import org.chorus_oss.chorus.nbt.tag.CompoundTag
import org.chorus_oss.protocol.types.biome.BiomeWeightedData

fun BiomeWeightedData.Companion.fromNBT(nbt: CompoundTag): BiomeWeightedData {
    return BiomeWeightedData(
        biome = nbt.getShort("biomeIdentifier"),
        weight = nbt.getInt("weight").toUInt()
    )
}