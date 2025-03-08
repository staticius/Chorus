package cn.nukkit.entity.ai.executor

import cn.nukkit.entity.mob.EntityMob
import lombok.extern.slf4j.Slf4j
import java.util.concurrent.*

@Slf4j
class MultipleExecutor : IBehaviorExecutor {
    protected var executors: Set<IBehaviorExecutor>

    constructor(executors: Set<IBehaviorExecutor>) {
        this.executors = executors
    }

    constructor(vararg executors: IBehaviorExecutor?) {
        this.executors = java.util.Set.of(*executors)
    }

    override fun execute(entity: EntityMob?): Boolean {
        val tasks = ArrayList<CompletableFuture<*>>()
        for (executor in executors) {
            tasks.add(CompletableFuture.supplyAsync { executor.execute(entity) })
        }
        try {
            return CompletableFuture.allOf(*tasks.toTypedArray<CompletableFuture<*>>())
                .whenComplete { s: Void?, t: Throwable? ->
                    if (t != null) {
                        MultipleExecutor.log.error("阶段执行过程中存在异常：", t)
                    }
                }.thenApply<Boolean> { v: Void? ->
                    tasks.stream().map<Boolean> { task: CompletableFuture<*> ->
                        try {
                            return@map task.get() as Boolean
                        } catch (e: InterruptedException) {
                            throw RuntimeException(e)
                        } catch (e: ExecutionException) {
                            throw RuntimeException(e)
                        }
                    }.reduce(false) { a: Boolean, b: Boolean -> a || b }
                }.get()
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        } catch (e: ExecutionException) {
            throw RuntimeException(e)
        }
    }

    override fun onInterrupt(entity: EntityMob?) {
        super.onInterrupt(entity)
    }

    override fun onStop(entity: EntityMob?) {
        super.onStop(entity)
    }
}
