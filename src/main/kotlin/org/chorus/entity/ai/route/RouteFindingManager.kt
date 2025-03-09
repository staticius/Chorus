package org.chorus.entity.ai.route

import cn.nukkit.Server
import cn.nukkit.entity.ai.route.finder.IRouteFinder
import cn.nukkit.math.*
import java.security.*
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory
import java.util.concurrent.ForkJoinWorkerThread
import java.util.concurrent.RecursiveAction
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * 寻路管理器，所有的寻路任务都应该提交到这个管理器中，管理器负责调度寻路任务，实现资源利用最大化
 */
class RouteFindingManager protected constructor() {
    protected val pool: ForkJoinPool

    init {
        pool = ForkJoinPool(Runtime.getRuntime().availableProcessors(), RouteFindingPoolThreadFactory(), null, true)
    }

    fun submit(task: RouteFindingTask) {
        task.setStartTime(Server.getInstance().nextTick)
        pool.submit(task)
    }

    class RouteFindingThread internal constructor(pool: ForkJoinPool) : ForkJoinWorkerThread(pool) {
        /**
         * Creates a ForkJoinWorkerThread operating in the given pool.
         *
         * @param pool the pool this thread works in
         * @throws NullPointerException if pool is null
         */
        init {
            this.name = "RouteFindingThread" + threadCount.getAndIncrement()
            this.priority = 2 // 保证主线程能得到足够多的CPU时间
        }
    }

    class RouteFindingPoolThreadFactory : ForkJoinWorkerThreadFactory {
        override fun newThread(pool: ForkJoinPool): ForkJoinWorkerThread {
            return AccessController.doPrivileged(
                PrivilegedAction<ForkJoinWorkerThread> { RouteFindingThread(pool) },
                ACC
            )
        }

        companion object {
            private val ACC = contextWithPermissions(
                RuntimePermission("getClassLoader"),
                RuntimePermission("setContextClassLoader")
            )

            fun contextWithPermissions(vararg perms: Permission?): AccessControlContext {
                val permissions = Permissions()
                for (perm in perms) permissions.add(perm)
                return AccessControlContext(arrayOf(ProtectionDomain(null, permissions)))
            }
        }
    }

    class RouteFindingTask(private val routeFinder: IRouteFinder?, private val onFinish: FinishCallback) :
        RecursiveAction() {
        private val startTime = AtomicLong(0)
        private val started = AtomicBoolean(false)
        private val finished = AtomicBoolean(false)
        var start: Vector3? = null
            private set
        var target: Vector3? = null
            private set

        fun setStart(start: Vector3?): RouteFindingTask {
            this.start = start
            return this
        }

        fun setTarget(target: Vector3?): RouteFindingTask {
            this.target = target
            return this
        }

        /**
         * @return 是否开始寻路
         */
        fun getStarted(): Boolean {
            return started.get()
        }

        fun setStarted(started: Boolean) {
            this.started.set(started)
        }

        /**
         * @return 是否已经完成寻路，寻路失败也会返回完成
         */
        fun getFinished(): Boolean {
            return finished.get()
        }

        protected fun setFinished(finished: Boolean) {
            this.finished.set(finished)
        }

        fun getStartTime(): Long {
            return startTime.get()
        }

        fun setStartTime(startTime: Long): RouteFindingTask {
            this.startTime.set(startTime)
            return this
        }

        override fun compute() {
            setStarted(true)
            routeFinder.setStart(start)
            routeFinder.setTarget(target)
            routeFinder!!.search()
            setFinished(true)
            onFinish.onFinish(this)
        }

        interface FinishCallback {
            fun onFinish(task: RouteFindingTask?)
        }
    }

    companion object {
        private val threadCount = AtomicInteger(0)
        var instance: RouteFindingManager = RouteFindingManager()
            protected set
    }
}
