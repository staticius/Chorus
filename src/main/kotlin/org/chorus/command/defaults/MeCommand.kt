package org.chorus.command.defaults

import org.chorus.command.*
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import org.chorus.lang.TranslationContainer
import org.chorus.utils.TextFormat
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/12
 */
class MeCommand(name: String) : VanillaCommand(name, "commands.me.description", "nukkit.command.me.usage") {
    init {
        this.permission = "nukkit.command.me"
        commandParameters.clear()
        commandParameters["message"] = arrayOf(
            CommandParameter.Companion.newType("message", CommandParamType.MESSAGE)
        )
        commandParameters["default"] = CommandParameter.Companion.EMPTY_ARRAY
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList>,
        log: CommandLogger
    ): Int {
        val name = sender.name
        var message: String? = ""
        if (result.key == "message") {
            message = result.value!!.getResult(0)
        }

        broadcastCommandMessage(
            sender,
            TranslationContainer("chat.type.emote", name, TextFormat.WHITE.toString() + message),
            true
        )
        return 1
    }
}
