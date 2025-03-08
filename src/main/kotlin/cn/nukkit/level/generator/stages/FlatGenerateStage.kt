package cn.nukkit.level.generator.stages

import cn.nukkit.block.*
import cn.nukkit.level.biome.BiomeID
import cn.nukkit.level.format.*
import cn.nukkit.level.generator.ChunkGenerateContext
import cn.nukkit.level.generator.GenerateStage

class FlatGenerateStage : GenerateStage() {
    override fun name(): String {
        return NAME
    }

    override fun apply(context: ChunkGenerateContext) {
        val chunk = context.chunk
        for (x in 0..15) {
            for (z in 0..15) {
                chunk.setHeightMap(x, z, 5)
                for (y in 0..4) {
                    if (y == 0) {
                        chunk.setBlockState(x, y, z, bedrock)
                    } else if (y == 4) chunk.setBlockState(x, y, z, grass)
                    else chunk.setBlockState(x, y, z, dirt)
                }
                for (i in context.generator.dimensionData.minSectionY..<context.generator.dimensionData.minSectionY) {
                    chunk.setBiomeId(x, i, z, BiomeID.Companion.PLAINS)
                }
            }
        }
        chunk.chunkState = ChunkState.POPULATED
    }

    companion object {
        const val NAME: String = "flat_generate"

        val bedrock: BlockState = BlockBedrock.PROPERTIES.getDefaultState()
        val grass: BlockState = BlockGrassBlock.PROPERTIES.getDefaultState()
        val dirt: BlockState = BlockDirt.PROPERTIES.getDefaultState()
    }
}
