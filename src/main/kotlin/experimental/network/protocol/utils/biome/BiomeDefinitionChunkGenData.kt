package org.chorus_oss.chorus.experimental.network.protocol.utils.biome

import org.chorus_oss.chorus.nbt.tag.CompoundTag
import org.chorus_oss.protocol.types.biome.BiomeCappedSurfaceData
import org.chorus_oss.protocol.types.biome.BiomeClimateData
import org.chorus_oss.protocol.types.biome.BiomeConsolidatedFeaturesData
import org.chorus_oss.protocol.types.biome.BiomeDefinitionChunkGenData
import org.chorus_oss.protocol.types.biome.BiomeLegacyWorldGenRulesData
import org.chorus_oss.protocol.types.biome.BiomeMesaSurfaceData
import org.chorus_oss.protocol.types.biome.BiomeMountainsParamData
import org.chorus_oss.protocol.types.biome.BiomeMultinoiseGenRulesData
import org.chorus_oss.protocol.types.biome.BiomeOverworldGenRulesData
import org.chorus_oss.protocol.types.biome.BiomeSurfaceMaterialAdjustmentData
import org.chorus_oss.protocol.types.biome.BiomeSurfaceMaterialData

fun BiomeDefinitionChunkGenData.Companion.fromNBT(nbt: CompoundTag): BiomeDefinitionChunkGenData {
    return BiomeDefinitionChunkGenData(
        climate = if (nbt.containsCompound("climate")) BiomeClimateData.fromNBT(nbt.getCompound("climate")) else null,
        consolidatedFeatures = if (nbt.containsCompound("consolidatedFeatures")) BiomeConsolidatedFeaturesData.fromNBT(nbt.getCompound("consolidatedFeatures")) else null,
        mountainParams = if (nbt.containsCompound("mountainParams")) BiomeMountainsParamData.fromNBT(nbt.getCompound("mountainParams")) else null,
        surfaceMaterialAdjustments = if (nbt.containsCompound("surfaceMaterialAdjustments")) BiomeSurfaceMaterialAdjustmentData.fromNBT(nbt.getCompound("surfaceMaterialAdjustments")) else null,
        surfaceMaterials = if (nbt.containsCompound("surfaceMaterials")) BiomeSurfaceMaterialData.fromNBT(nbt.getCompound("surfaceMaterials")) else null,
        hasSwampSurface = nbt.getBoolean("hasSwampSurface"),
        hasFrozenOceanSurface = nbt.getBoolean("hasFrozenOceanSurface"),
        hasTheEndSurface = nbt.getBoolean("hasTheEndSurface"),
        mesaSurface = if (nbt.containsCompound("mesaSurface")) BiomeMesaSurfaceData.fromNBT(nbt.getCompound("mesaSurface")) else null,
        cappedSurface = if (nbt.containsCompound("cappedSurface")) BiomeCappedSurfaceData.fromNBT(nbt.getCompound("cappedSurface")) else null,
        overworldGenRules = if (nbt.containsCompound("overworldGenRules")) BiomeOverworldGenRulesData.fromNBT(nbt.getCompound("overworldGenRules")) else null,
        multinoiseGenRules = if (nbt.containsCompound("multinoiseGenRules")) BiomeMultinoiseGenRulesData.fromNBT(nbt.getCompound("multinoiseGenRules")) else null,
        legacyWorldGenRules = if (nbt.containsCompound("legacyWorldGenRules")) BiomeLegacyWorldGenRulesData.fromNBT(nbt.getCompound("legacyWorldGenRules")) else null,
    )
}