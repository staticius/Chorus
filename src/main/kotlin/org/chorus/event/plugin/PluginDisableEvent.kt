package org.chorus.event.plugin

import org.chorus.event.HandlerList
import org.chorus.plugin.Plugin


class PluginDisableEvent(plugin: Plugin) : PluginEvent(plugin) {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
