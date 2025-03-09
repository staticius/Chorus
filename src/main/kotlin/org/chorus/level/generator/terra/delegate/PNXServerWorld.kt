package org.chorus.level.generator.terra.delegate

import org.chorus.block.BlockID
import org.chorus.entity.Entity.Companion.createEntity
import org.chorus.level.*
import org.chorus.level.generator.terra.PNXAdapter
import org.chorus.level.generator.terra.TerraGenerator
import org.chorus.math.BlockVector3
import com.dfsek.terra.api.block.entity.BlockEntity
import com.dfsek.terra.api.block.state.BlockState
import com.dfsek.terra.api.config.ConfigPack
import com.dfsek.terra.api.entity.Entity
import com.dfsek.terra.api.entity.EntityType
import com.dfsek.terra.api.world.ServerWorld
import com.dfsek.terra.api.world.biome.generation.BiomeProvider
import com.dfsek.terra.api.world.chunk.Chunk
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator

@JvmRecord
data class PNXServerWorld(val generatorWrapper: TerraGenerator, val level: Level) : ServerWorld {
    override fun setBlockState(i: Int, i1: Int, i2: Int, blockState: BlockState, b: Boolean) {
        val innerBlockState = (blockState as PNXBlockStateDelegate).innerBlockState
        if (innerBlockState.identifier == BlockID.KELP) {
            level.setBlockStateAt(i, i1, i2, innerBlockState)
            level.setBlockStateAt(i, i1, i2, 1, PNXProtoChunk.Companion.water)
        } else {
            val ob = level.getBlockStateAt(i, i1, i2)
            if (ob!!.identifier == BlockID.WATERLILY ||
                ob.identifier == BlockID.WATER ||
                ob.identifier == BlockID.FLOWING_WATER
            ) {
                level.setBlockStateAt(i, i1, i2, innerBlockState)
                level.setBlockStateAt(i, i1, i2, 1, PNXProtoChunk.Companion.water)
            } else {
                level.setBlockStateAt(i, i1, i2, innerBlockState)
            }
        }
    }

    override fun spawnEntity(v: Double, v1: Double, v2: Double, entityType: EntityType): Entity {
        val identifier = entityType.handle as String
        val nukkitEntity = createEntity(identifier, Locator(v, v1, v2, level))
        return PNXEntity(nukkitEntity!!, this)
    }

    override fun getBlockState(i: Int, i1: Int, i2: Int): BlockState {
        return PNXAdapter.adapt(level.getBlockStateAt(i, i1, i2))
    }

    override fun getBlockEntity(i: Int, i1: Int, i2: Int): BlockEntity? {
        val blockEntity = level.getBlockEntity(BlockVector3(i, i1, i2))
        return null
    }

    override fun getGenerator(): ChunkGenerator {
        return generatorWrapper.handle
    }

    override fun getBiomeProvider(): BiomeProvider {
        return generatorWrapper.biomeProvider
    }

    override fun getPack(): ConfigPack {
        return generatorWrapper.configPack
    }

    override fun getSeed(): Long {
        return level.seed
    }

    override fun getMaxHeight(): Int {
        return generatorWrapper.dimensionData.maxHeight
    }

    override fun getMinHeight(): Int {
        return generatorWrapper.dimensionData.minHeight
    }

    override fun getHandle(): Level {
        return level
    }

    override fun getChunkAt(i: Int, i1: Int): Chunk {
        return PNXChunkDelegate(this, level.getChunk(i, i1)!!)
    }
}
