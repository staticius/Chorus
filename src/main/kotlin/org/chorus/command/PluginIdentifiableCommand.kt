package org.chorus.command

import cn.nukkit.plugin.Plugin

/**
 * @author MagicDroidX (Nukkit Project)
 */
interface PluginIdentifiableCommand {
    @JvmField
    val plugin: Plugin?
}
