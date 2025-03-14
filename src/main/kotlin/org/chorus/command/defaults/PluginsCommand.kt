package org.chorus.command.defaults

import org.chorus.command.Command
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import org.chorus.utils.TextFormat
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/12
 */
class PluginsCommand(name: String) : Command(
    name,
    "%nukkit.command.plugins.description",
    "%nukkit.command.plugins.usage",
    arrayOf<String>("pl")
), CoreCommand {
    init {
        this.permission = "nukkit.command.plugins"
        commandParameters.clear()
        commandParameters["default"] = CommandParameter.Companion.EMPTY_ARRAY
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        this.sendPluginList(sender, log)
        return 1
    }

    private fun sendPluginList(sender: CommandSender, log: CommandLogger) {
        val list = StringBuilder()
        val plugins = sender.server.pluginManager.plugins
        for (plugin in plugins.values) {
            if (list.length > 0) {
                list.append(TextFormat.WHITE.toString() + ", ")
            }
            list.append(if (plugin.isEnabled) TextFormat.GREEN else TextFormat.RED)
            list.append(plugin.description.fullName)
        }
        log.addMessage("nukkit.command.plugins.success", plugins.size.toString(), list.toString()).output()
    }
}
