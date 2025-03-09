package org.chorus.level.generator.terra.delegate

import org.chorus.block.BlockFlowingWater
import org.chorus.block.BlockID
import org.chorus.level.format.IChunk
import org.chorus.level.generator.terra.PNXAdapter
import com.dfsek.terra.api.block.state.BlockState
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk

@JvmRecord
data class PNXProtoChunk(val chunk: IChunk) : ProtoChunk {
    override fun getMaxHeight(): Int {
        return chunk.dimensionData.maxHeight
    }

    override fun setBlock(i: Int, i1: Int, i2: Int, blockState: BlockState) {
        val innerBlockState = (blockState as PNXBlockStateDelegate).innerBlockState
        if (innerBlockState.identifier == BlockID.KELP) {
            chunk.setBlockState(i, i1, i2, innerBlockState)
            chunk.setBlockState(i, i1, i2, water, 1)
        } else {
            val ob = chunk.getBlockState(i, i1, i2)
            if (ob!!.identifier == BlockID.WATERLILY ||
                ob.identifier == BlockID.WATER ||
                ob.identifier == BlockID.FLOWING_WATER
            ) {
                chunk.setBlockState(i, i1, i2, innerBlockState)
                chunk.setBlockState(i, i1, i2, water, 1)
            } else {
                chunk.setBlockState(i, i1, i2, innerBlockState)
            }
        }
    }

    override fun getBlock(i: Int, i1: Int, i2: Int): BlockState {
        return PNXAdapter.adapt(chunk.getBlockState(i, i1, i2))
    }

    override fun getHandle(): Any {
        return chunk
    }

    companion object {
        var water: cn.nukkit.block.BlockState = BlockFlowingWater.properties.defaultState
    }
}
