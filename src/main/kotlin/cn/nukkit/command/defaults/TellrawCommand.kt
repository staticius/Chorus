package cn.nukkit.command.defaults

import cn.nukkit.Player
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.tree.node.PlayersNode
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.command.utils.RawText
import cn.nukkit.lang.TranslationContainer
import cn.nukkit.utils.TextFormat
import com.google.gson.JsonSyntaxException
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.set

class TellrawCommand(name: String) : VanillaCommand(name, "commands.tellraw.description") {
    init {
        this.permission = "nukkit.command.tellraw"
        commandParameters.clear()
        commandParameters["default"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("player", CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newType("rawtext", CommandParamType.RAWTEXT)
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
        try {
            val players = list!!.getResult<List<Player>>(0)!!
            if (players.isEmpty()) {
                log.addNoTargetMatch().output()
                return 0
            }
            val rawTextObject = list.getResult<RawText>(1)
            rawTextObject!!.preParse(sender)
            for (player in players) {
                player.sendRawTextMessage(rawTextObject)
            }
            return 1
        } catch (e: JsonSyntaxException) {
            sender.sendMessage(TranslationContainer(TextFormat.RED.toString() + "%commands.tellraw.jsonStringException"))
            return 0
        }
    }
}
