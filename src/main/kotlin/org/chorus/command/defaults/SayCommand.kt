package org.chorus.command.defaults

import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import org.chorus.lang.TranslationContainer
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/12
 */
class SayCommand(name: String) : VanillaCommand(name, "commands.say.description") {
    init {
        this.permission = "nukkit.command.say"
        commandParameters.clear()
        commandParameters["default"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("message", CommandParamType.MESSAGE)
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val senderString = sender.name
        val message = result.value!!.getResult<String>(0)
        sender.server.broadcastMessage(TranslationContainer("%chat.type.announcement", senderString, message))
        return 1
    }
}
