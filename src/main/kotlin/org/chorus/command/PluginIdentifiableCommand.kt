package org.chorus.command

import org.chorus.plugin.Plugin

/**
 * @author MagicDroidX (Nukkit Project)
 */
interface PluginIdentifiableCommand {
    @JvmField
    val plugin: Plugin?
}
