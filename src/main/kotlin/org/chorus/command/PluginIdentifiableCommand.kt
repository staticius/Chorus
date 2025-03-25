package org.chorus.command

import org.chorus.plugin.Plugin


interface PluginIdentifiableCommand {
    val plugin: Plugin
}
