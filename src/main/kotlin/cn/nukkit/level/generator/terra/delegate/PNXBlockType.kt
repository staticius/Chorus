package cn.nukkit.level.generator.terra.delegate

import cn.nukkit.block.BlockFlowingWater
import cn.nukkit.level.generator.terra.PNXAdapter
import com.dfsek.terra.api.block.BlockType
import com.dfsek.terra.api.block.state.BlockState

@JvmRecord
data class PNXBlockType(val innerBlockState: cn.nukkit.block.BlockState) : BlockType {
    override fun getDefaultState(): BlockState {
        return PNXAdapter.adapt(innerBlockState)
    }

    override fun isSolid(): Boolean {
        return innerBlockState.toBlock().isSolid
    }

    override fun isWater(): Boolean {
        return innerBlockState.toBlock() is BlockFlowingWater
    }

    override fun getHandle(): cn.nukkit.block.BlockState {
        return innerBlockState
    }
}
