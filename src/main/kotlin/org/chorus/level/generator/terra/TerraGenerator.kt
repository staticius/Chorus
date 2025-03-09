package org.chorus.level.generator.terra

import cn.nukkit.Server
import cn.nukkit.level.*
import cn.nukkit.level.format.*
import cn.nukkit.level.generator.ChunkGenerateContext
import cn.nukkit.level.generator.GenerateStage
import cn.nukkit.level.generator.Generator
import cn.nukkit.level.generator.terra.delegate.PNXProtoChunk
import cn.nukkit.level.generator.terra.delegate.PNXProtoWorld
import cn.nukkit.level.generator.terra.delegate.PNXServerWorld
import com.dfsek.terra.api.config.ConfigPack
import com.dfsek.terra.api.world.biome.generation.BiomeProvider
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator
import com.dfsek.terra.api.world.chunk.generation.util.GeneratorWrapper
import com.dfsek.terra.api.world.info.WorldProperties
import lombok.extern.slf4j.Slf4j
import java.util.*
import java.util.function.Supplier

@Slf4j
class TerraGenerator(dimensionData: DimensionData, options: Map<String?, Any>) :
    Generator(dimensionData, options), GeneratorWrapper {
    val biomeProvider: BiomeProvider
    val configPack: ConfigPack
    private val worldProperties: WorldProperties
    private val chunkGenerator: ChunkGenerator

    init {
        var packName = "default"
        if (options.containsKey("pack")) {
            packName = options["pack"].toString()
        }
        this.configPack = createConfigPack(packName)
        this.chunkGenerator = createGenerator(this.configPack)
        this.biomeProvider = configPack.biomeProvider
        this.worldProperties = object : WorldProperties {
            override fun getSeed(): Long {
                return level.seed
            }

            override fun getMaxHeight(): Int {
                return dimensionData.maxHeight
            }

            override fun getMinHeight(): Int {
                return dimensionData.minHeight
            }

            override fun getHandle(): Any {
                return null
            }
        }
    }

    override fun stages(builder: GenerateStage.Builder) {
        builder.start(TerraStage())
    }

    internal inner class TerraStage : GenerateStage() {
        override fun apply(context: ChunkGenerateContext) {
            val chunk = context.chunk
            val chunkX = chunk.x
            val chunkZ = chunk.z
            chunkGenerator.generateChunkData(PNXProtoChunk(chunk), worldProperties, biomeProvider, chunkX, chunkZ)
            val minHeight = level.minHeight
            val maxHeight = level.maxHeight
            for (x in 0..15) {
                for (y in minHeight..<maxHeight) {
                    for (z in 0..15) {
                        chunk.setBiomeId(
                            x,
                            y,
                            z,
                            biomeProvider.getBiome(
                                chunkX * 16 + x,
                                y,
                                chunkZ * 16 + z,
                                level.seed
                            ).platformBiome.handle as Int
                        )
                    }
                }
            }
            val tmp = PNXProtoWorld(PNXServerWorld(this@TerraGenerator, context.level), chunkX, chunkZ)
            try {
                for (generationStage in configPack.stages) {
                    generationStage.populate(tmp)
                }
            } catch (e: Exception) {
                TerraGenerator.log.error("", e)
            }

            if (Server.getInstance().settings.chunkSettings().lightUpdates()) {
                chunk.recalculateHeightMap()
                chunk.populateSkyLight()
                chunk.setLightPopulated()
            }

            chunk.chunkState = ChunkState.FINISHED
        }

        override fun name(): String {
            return "terra_stage"
        }
    }

    override val name: String
        get() = "terra"

    override fun getHandle(): ChunkGenerator {
        return chunkGenerator
    }

    override val dimensionData: DimensionData
        get() = dimensionData

    override var level: Level
        get() = level
        set(level) {
            super.level = level
        }

    companion object {
        private fun createConfigPack(packName: String): ConfigPack {
            val byID: Optional<ConfigPack> = PNXPlatform.Companion.getInstance().getConfigRegistry().getByID(packName)
            return byID.orElseGet {
                PNXPlatform.Companion.getInstance().getConfigRegistry().getByID(packName.toUpperCase(Locale.ENGLISH))
                    .orElseThrow<IllegalArgumentException>(Supplier<IllegalArgumentException> {
                        IllegalArgumentException(
                            "Can't find terra config pack $packName"
                        )
                    })
            }
        }


        private fun createGenerator(config: ConfigPack): ChunkGenerator {
            return config.generatorProvider.newInstance(config)
        }
    }
}
