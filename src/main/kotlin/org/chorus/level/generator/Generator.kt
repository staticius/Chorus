package org.chorus.level.generator

import com.google.common.base.Preconditions
import org.chorus.block.BlockID
import org.chorus.level.DimensionData
import org.chorus.level.Level
import org.chorus.level.format.IChunk
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

abstract class Generator(val dimensionData: DimensionData, val settings: Map<String?, Any>?) :
    BlockID {
    protected val start: GenerateStage
    protected val end: GenerateStage?
    protected var level: Level? = null

    init {
        val builder = GenerateStage.Builder()
        stages(builder)
        this.start = builder.start!!
        this.end = builder.end
    }

    fun setLevel(level: Level?) {
        this.level = level
    }

    abstract fun stages(builder: GenerateStage.Builder)

    abstract val name: String

    @JvmOverloads
    fun syncGenerate(chunk: IChunk, to: String? = end!!.name()): IChunk {
        val context = ChunkGenerateContext(this, level, chunk)
        var future = CompletableFuture.runAsync(
            {
                start.apply(context)
            }, start.executor
        )
        var now: GenerateStage? = start
        while ((now?.nextStage.also { now = it }) != null) {
            val finalNow = now!!
            if (finalNow.name() == to) {
                future = future.thenRunAsync({ finalNow.apply(context) }, finalNow.executor)
                break
            }
            future = future.thenRunAsync({ finalNow.apply(context) }, finalNow.executor)
        }
        future.join()
        return context.chunk
    }

    fun asyncGenerate(chunk: IChunk, callback: Consumer<ChunkGenerateContext>) {
        asyncGenerate(chunk, end!!.name(), callback)
    }

    @JvmOverloads
    fun asyncGenerate(
        chunk: IChunk,
        to: String? = end!!.name(),
        callback: Consumer<ChunkGenerateContext> = Consumer { }
    ) {
        Preconditions.checkNotNull(to)
        val context = ChunkGenerateContext(this, level, chunk)
        asyncGenerate0(context, start, to) { callback.accept(context) }
    }

    private fun asyncGenerate0(context: ChunkGenerateContext, start: GenerateStage?, to: String?, callback: Runnable) {
        if (start == null || to == null) return
        if (to == start.name()) {
            start.executor.execute {
                start.apply(context)
                callback.run()
            }
            return
        }
        start.executor.execute {
            start.apply(context)
            asyncGenerate0(context, start.nextStage, to, callback)
        }
    }
}
