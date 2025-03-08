package cn.nukkit.plugin

import cn.nukkit.event.*
import cn.nukkit.utils.EventException

/**
 * @author MagicDroidX (Nukkit Project)
 */
class RegisteredListener(
    val listener: Listener,
    private var executor: EventExecutor,
    val priority: EventPriority?,
    val plugin: Plugin,
    val isIgnoringCancelled: Boolean
) {
    @Throws(EventException::class)
    fun callEvent(event: Event) {
        if (event is Cancellable) {
            if (event.isCancelled && isIgnoringCancelled) {
                return
            }
        }
        try {
            executor.execute(listener, event)
        } catch (e: IllegalAccessError) { // 动态编译的字节码调用失败时的逃生门
            if (executor is CompiledExecutor) {
                executor = MethodEventExecutor(executor.getOriginMethod())
                executor.execute(listener, event)
            }
        } catch (e: NoSuchMethodError) {
            if (executor is CompiledExecutor) {
                executor = MethodEventExecutor(executor.getOriginMethod())
                executor.execute(listener, event)
            }
        }
    }

    companion object {
        val EMPTY_ARRAY: Array<RegisteredListener?> = arrayOfNulls(0)
    }
}
