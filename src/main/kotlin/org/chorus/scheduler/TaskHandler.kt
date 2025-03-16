package org.chorus.scheduler

import org.chorus.plugin.Plugin
import org.chorus.utils.Loggable


/**
 * @author MagicDroidX
 */

class TaskHandler(val plugin: Plugin?, val task: Runnable, val taskId: Int, val isAsynchronous: Boolean) {
    var delay: Int = 0
    var period: Int = 0

    var lastRunTick: Int = 0
    var nextRunTick: Int = 0

    var isCancelled: Boolean = false
        private set

    val isDelayed: Boolean
        get() = this.delay > 0

    val isRepeating: Boolean
        get() = this.period > 0

    fun cancel() {
        if (!this.isCancelled && task is Task) {
            task.onCancel()
        }
        this.isCancelled = true
    }

    fun run(currentTick: Int) {
        try {
            lastRunTick = currentTick
            task.run()
        } catch (ex: RuntimeException) {
            TaskHandler.log.error("Exception while invoking run", ex)
        }
    }

    companion object : Loggable
}
