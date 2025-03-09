package org.chorus.level.generator.terra.delegate

import cn.nukkit.level.format.IChunk
import cn.nukkit.level.generator.terra.PNXAdapter
import com.dfsek.terra.api.block.state.BlockState
import com.dfsek.terra.api.world.ServerWorld
import com.dfsek.terra.api.world.chunk.Chunk

@JvmRecord
data class PNXChunkDelegate(val world: ServerWorld, val chunk: IChunk) : Chunk {
    override fun setBlock(i: Int, i1: Int, i2: Int, blockState: BlockState, b: Boolean) {
        setBlock(i, i1, i2, blockState)
    }

    override fun setBlock(i: Int, i1: Int, i2: Int, blockState: BlockState) {
        chunk.setBlockState(i, i1, i2, (blockState as PNXBlockStateDelegate).handle)
    }

    override fun getBlock(i: Int, i1: Int, i2: Int): BlockState {
        return PNXAdapter.adapt(chunk.getBlockState(i, i1, i2))
    }

    override fun getX(): Int {
        return 0
    }

    override fun getZ(): Int {
        return 0
    }

    override fun getWorld(): ServerWorld {
        return world
    }

    override fun getHandle(): Any {
        return chunk
    }
}
