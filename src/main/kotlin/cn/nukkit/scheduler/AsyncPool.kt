package cn.nukkit.scheduler

import lombok.extern.slf4j.Slf4j
import java.util.concurrent.*

/**
 * @author Nukkit Project Team
 */
@Slf4j
class AsyncPool(size: Int) :
    ThreadPoolExecutor(size, Int.MAX_VALUE, 60, TimeUnit.MILLISECONDS, SynchronousQueue<Runnable>()) {
    init {
        this.threadFactory = ThreadFactory { runnable: Runnable? ->
            object : Thread(runnable) {
                init {
                    isDaemon = true
                    name = String.format("Nukkit Asynchronous Task Handler #%s", poolSize)
                }
            }
        }
    }

    override fun afterExecute(runnable: Runnable, throwable: Throwable?) {
        if (throwable != null) {
            AsyncPool.log.error("Exception in asynchronous task", throwable)
        }
    }
}