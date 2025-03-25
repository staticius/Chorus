package org.chorus.command.defaults

import org.chorus.Server
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import kotlin.collections.set

class SaveOnCommand(name: String) : VanillaCommand(name, "Enable auto saving") {
    init {
        this.permission = "nukkit.command.save.enable"
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
        Server.instance.setAutoSave(true)
        log.addSuccess("commands.save.enabled").output(true)
        return 1
    }
}
