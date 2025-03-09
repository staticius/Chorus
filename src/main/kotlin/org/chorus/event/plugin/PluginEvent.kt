package org.chorus.event.plugin

import org.chorus.event.Event
import org.chorus.event.HandlerList
import org.chorus.plugin.Plugin

/**
 * @author MagicDroidX (Nukkit Project)
 */
open class PluginEvent(val plugin: Plugin) : Event() {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
