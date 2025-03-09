package org.chorus

import org.chorus.command.SimpleCommandMap
import org.chorus.event.Event
import org.chorus.plugin.PluginManager
import com.google.common.base.Preconditions

class TestPluginManager(server: Server?, commandMap: SimpleCommandMap?) :
    PluginManager(server, commandMap) {
    override fun callEvent(event: Event) {
        Preconditions.checkNotNull(event)
        val i = counts.computeIfAbsent(event.javaClass) { e: Class<out Event>? -> 0 }
        counts[event.javaClass] = i + 1
        val testEventHandler = handlers[event.javaClass] ?: return
        val castEvent = testEventHandler.eventClass.cast(event) as Event
        testEventHandler.handle(castEvent)
    }

    fun getCount(clazz: Class<out Event>): Int {
        return counts.computeIfAbsent(clazz) { e: Class<out Event>? -> 0 }
    }

    fun resetCount(clazz: Class<out Event>) {
        counts[clazz] = 0
    }

    fun resetAll() {
        counts.clear()
        handlers.clear()
    }

    fun registerTestEventHandler(consumers: List<TestEventHandler<out Event?>>) {
        for (h in consumers) {
            handlers[h.eventClass] = h
        }
    }

    companion object {
        var handlers: MutableMap<Class<out Event?>?, TestEventHandler<*>> = HashMap()
        var counts: MutableMap<Class<out Event>, Int> = HashMap()
    }
}
