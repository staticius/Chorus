package org.chorus.level.generator

import com.google.common.base.Preconditions
import org.chorus.Server
import java.util.concurrent.Executor

abstract class GenerateStage {
    var nextStage: GenerateStage? = null
        private set

    abstract fun apply(context: ChunkGenerateContext)

    abstract fun name(): String

    open val executor: Executor
        get() = Server.instance.computeThreadPool

    private fun next(stage: GenerateStage) {
        if (this.nextStage == null) {
            this.nextStage = stage //next -> null
        } else {
            nextStage!!.next(stage) //next -> next
        }
    }

    override fun toString(): String {
        return name()
    }

    class Builder internal constructor() {
        private var start: GenerateStage? = null
        var end: GenerateStage? = null
            private set

        fun start(start: GenerateStage?): Builder {
            this.start = start
            end = start
            return this
        }

        fun next(next: GenerateStage): Builder {
            start!!.next(next)
            end = next
            return this
        }

        fun getStart(): GenerateStage {
            Preconditions.checkNotNull(start, "you must be set start generate stage!")
            return start!!
        }
    }
}
