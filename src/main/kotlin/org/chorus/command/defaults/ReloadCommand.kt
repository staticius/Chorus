package org.chorus.command.defaults

import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import org.chorus.utils.TextFormat
import kotlin.collections.Map
import kotlin.collections.set


class ReloadCommand(name: String) : VanillaCommand(name, "Reload the server/plugin") {
    init {
        this.permission = "nukkit.command.reload"
        commandParameters.clear()
        commandParameters["default"] = CommandParameter.Companion.EMPTY_ARRAY
        commandParameters["function"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum("function", false, arrayOf<String?>("function"))
        )
        commandParameters["plugin"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum("plugin", arrayOf<String?>("plugin")),
            CommandParameter.Companion.newType("plugin", CommandParamType.STRING)
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val list = result.value
        when (result.key) {
            "default" -> {
                log.addMessage(TextFormat.YELLOW.toString() + "%nukkit.command.reload.reloading" + TextFormat.WHITE)
                    .output(true)
                sender.server.reload()
                log.addMessage(TextFormat.YELLOW.toString() + "%nukkit.command.reload.reloaded" + TextFormat.WHITE)
                    .output(true)
                return 1
            }

            "function" -> {
                log.addMessage(TextFormat.YELLOW.toString() + "%nukkit.command.reload.reloading" + TextFormat.WHITE)
                    .output(true)
                sender.server.functionManager.reload()
                log.addMessage(TextFormat.YELLOW.toString() + "%nukkit.command.reload.reloaded" + TextFormat.WHITE)
                    .output(true)
                return 1
            }

            "plugin" -> {
                log.addMessage(TextFormat.YELLOW.toString() + "%nukkit.command.reload.reloading" + TextFormat.WHITE)
                    .output(true)
                val pluginManager = sender.server.pluginManager
                val str = list!!.getResult<String>(1)
                var plugin = pluginManager.getPlugin(str)
                //todo: 多语言
                log.addSuccess("Reloading plugin §a" + plugin.description.name).output(true)
                pluginManager.disablePlugin(plugin)
                pluginManager.plugins.remove(plugin.description.name)
                plugin = pluginManager.loadPlugin(plugin.file)
                pluginManager.enablePlugin(plugin)
                log.addSuccess("Plugin §a" + plugin.description.name + " §freloaded!").output(true)
                log.addSuccess(TextFormat.YELLOW.toString() + "%nukkit.command.reload.reloaded" + TextFormat.WHITE)
                    .output(true)
                return 1
            }

            else -> {
                return 0
            }
        }
    }
}
