package org.chorus.level.generator.stages

import org.chorus.level.format.ChunkState
import org.chorus.level.generator.ChunkGenerateContext
import org.chorus.level.generator.GenerateStage
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
