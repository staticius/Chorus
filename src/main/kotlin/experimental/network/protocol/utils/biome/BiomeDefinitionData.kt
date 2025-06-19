package org.chorus_oss.chorus.experimental.network.protocol.utils.biome

import org.chorus_oss.chorus.nbt.tag.CompoundTag
import org.chorus_oss.protocol.types.biome.BiomeDefinitionChunkGenData
import org.chorus_oss.protocol.types.biome.BiomeDefinitionData
import org.chorus_oss.protocol.types.biome.BiomeTagsData

fun BiomeDefinitionData.Companion.fromNBT(nbt: CompoundTag): BiomeDefinitionData {
    return BiomeDefinitionData(
        temperature = nbt.getFloat("temperature"),
        downfall = nbt.getFloat("downfall"),
        redSporeDensity = nbt.getFloat("redSporeDensity"),
        blueSporeDensity = nbt.getFloat("blueSporeDensity"),
        ashDensity = nbt.getFloat("ashSporeDensity"),
        whiteAshDensity = nbt.getFloat("whiteAshSporeDensity"),
        depth = nbt.getFloat("depth"),
        scale = nbt.getFloat("scale"),
        mapWaterColorARGB = nbt.getInt("mapWaterColorARGB"),
        rain = nbt.getBoolean("rain"),
        tags = if (nbt.containsCompound("tags")) BiomeTagsData.fromNBT(nbt.getCompound("tags")) else null,
        chunkGenData = if (nbt.containsCompound("chunkGenData")) BiomeDefinitionChunkGenData.fromNBT(nbt.getCompound("chunkGenData")) else null,
    )
}