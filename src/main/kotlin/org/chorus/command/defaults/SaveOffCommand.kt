package org.chorus.command.defaults

import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/13
 */
class SaveOffCommand(name: String) : VanillaCommand(name, "Disable auto saving") {
    init {
        this.permission = "nukkit.command.save.disable"
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
        sender.server.autoSave = false
        log.addSuccess("commands.save.disabled").output(true)
        return 1
    }
}
