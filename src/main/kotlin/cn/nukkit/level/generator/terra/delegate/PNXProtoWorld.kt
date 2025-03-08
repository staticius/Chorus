package cn.nukkit.level.generator.terra.delegate

import cn.nukkit.entity.Entity.Companion.createEntity
import cn.nukkit.level.Locator
import com.dfsek.terra.api.block.entity.BlockEntity
import com.dfsek.terra.api.block.state.BlockState
import com.dfsek.terra.api.config.ConfigPack
import com.dfsek.terra.api.entity.Entity
import com.dfsek.terra.api.entity.EntityType
import com.dfsek.terra.api.world.ServerWorld
import com.dfsek.terra.api.world.biome.generation.BiomeProvider
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld

@JvmRecord
data class PNXProtoWorld(val serverWorld: ServerWorld, val centerChunkX: Int, val centerChunkZ: Int) : ProtoWorld {
    override fun centerChunkX(): Int {
        return centerChunkX
    }

    override fun centerChunkZ(): Int {
        return centerChunkZ
    }

    override fun getWorld(): ServerWorld {
        return serverWorld
    }

    override fun setBlockState(x: Int, y: Int, z: Int, blockState: BlockState, b: Boolean) {
        serverWorld.setBlockState(x, y, z, blockState, b)
    }

    override fun spawnEntity(v: Double, v1: Double, v2: Double, entityType: EntityType): Entity {
        val identifier = entityType.handle as String
        val nukkitEntity =
            createEntity(identifier, Locator(v, v1, v2, (serverWorld as PNXServerWorld).generatorWrapper.level))
        return PNXEntity(nukkitEntity!!, serverWorld)
    }

    override fun getBlockState(i: Int, i1: Int, i2: Int): BlockState {
        return serverWorld.getBlockState(i, i1, i2)
    }

    override fun getBlockEntity(i: Int, i1: Int, i2: Int): BlockEntity? {
        return null
    }

    override fun getGenerator(): ChunkGenerator {
        return serverWorld.generator
    }

    override fun getBiomeProvider(): BiomeProvider {
        return serverWorld.biomeProvider
    }

    override fun getPack(): ConfigPack {
        return serverWorld.pack
    }

    override fun getSeed(): Long {
        return handle.seed
    }

    override fun getMaxHeight(): Int {
        return serverWorld.maxHeight
    }

    override fun getMinHeight(): Int {
        return serverWorld.minHeight
    }

    override fun getHandle(): ServerWorld {
        return serverWorld
    }
}
