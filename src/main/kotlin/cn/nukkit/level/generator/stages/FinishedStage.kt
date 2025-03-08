package cn.nukkit.level.generator.stages

import cn.nukkit.level.format.ChunkState
import cn.nukkit.level.generator.ChunkGenerateContext
import cn.nukkit.level.generator.GenerateStage
import java.util.concurrent.Executor

class FinishedStage : GenerateStage() {
    override fun apply(context: ChunkGenerateContext) {
        val chunk = context.chunk
        chunk.chunkState = ChunkState.FINISHED
    }

    override val executor: Executor
        get() = Executor { obj: Runnable -> obj.run() }

    override fun name(): String {
        return NAME
    }

    companion object {
        const val NAME: String = "finished"
    }
}
