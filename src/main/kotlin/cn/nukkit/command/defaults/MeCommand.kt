package cn.nukkit.command.defaults

import cn.nukkit.command.*
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.lang.TranslationContainer
import cn.nukkit.utils.TextFormat
import kotlin.collections.Map
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/12
 */
class MeCommand(name: String) : VanillaCommand(name, "commands.me.description", "nukkit.command.me.usage") {
    init {
        this.permission = "nukkit.command.me"
        commandParameters.clear()
        commandParameters["message"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("message", CommandParamType.MESSAGE)
        )
        commandParameters["default"] = CommandParameter.Companion.EMPTY_ARRAY
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val name = sender.name
        var message: String? = ""
        if (result.key == "message") {
            message = result.value!!.getResult(0)
        }

        Command.Companion.broadcastCommandMessage(
            sender,
            TranslationContainer("chat.type.emote", name, TextFormat.WHITE.toString() + message),
            true
        )
        return 1
    }
}
