package org.chorus.command.defaults

import cn.nukkit.Player
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.tree.node.PlayersNode
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.lang.TranslationContainer
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/12
 */
class TellCommand(name: String) :
    VanillaCommand(name, "commands.tell.description", "", arrayOf<String>("w", "msg")) {
    init {
        this.permission = "nukkit.command.tell"
        commandParameters.clear()
        commandParameters["default"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("player", CommandParamType.TARGET, PlayersNode()),
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
        val list = result.value
        val players = list!!.getResult<List<Player>>(0)!!
        if (players.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        val msg = list.getResult<String>(1)
        for (player in players) {
            if (player === sender) {
                log.addError("commands.message.sameTarget").output()
                continue
            }
            log.addSuccess("commands.message.display.outgoing", player.name, msg)
            player.sendMessage(TranslationContainer("commands.message.display.incoming", sender.name, msg))
        }
        log.output()
        return 1
    }
}
