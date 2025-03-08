package cn.nukkit.level.generator.terra.mappings

import cn.nukkit.level.generator.terra.mappings.loader.BiomeRegistryLoader
import cn.nukkit.level.generator.terra.mappings.populator.BlockRegistryPopulator
import com.google.common.collect.HashBiMap
import java.util.function.Supplier


object MappingRegistries {
    /**
     * A mapped registry which stores Java biome identifiers and their Bedrock biome identifier.
     */
    val BIOME: SimpleMappingRegistry<HashBiMap<Int?, String?>> =
        SimpleMappingRegistry.Companion.create<String, HashBiMap<Int?, String?>>(
            "mappings/biomes.json",
            Supplier<RegistryLoader<String, HashBiMap<Int, String>>> { BiomeRegistryLoader() }
        )

    /**
     * A versioned registry which holds [BlockMappings] for each version. These block mappings contain
     * primarily Bedrock version-specific data.
     */
    val BLOCKS: BlockMappings? = BlockRegistryPopulator.initMapping()
}
