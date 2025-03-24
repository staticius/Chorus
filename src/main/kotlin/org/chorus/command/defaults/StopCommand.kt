package org.chorus.command.defaults

import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import kotlin.collections.set


class StopCommand(name: String) : VanillaCommand(name, "commands.stop.description") {
    init {
        this.permission = "nukkit.command.stop"
        commandParameters.clear()
        commandParameters["default"] = CommandParameter.Companion.EMPTY_ARRAY
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList>,
        log: CommandLogger
    ): Int {
        log.addSuccess("commands.stop.start").output(true)
        Server.instance.shutdown()
        return 1
    }
}
