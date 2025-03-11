package org.chorus.command

import org.chorus.plugin.Plugin


interface PluginIdentifiableCommand {
    @JvmField
    val plugin: Plugin?
}
