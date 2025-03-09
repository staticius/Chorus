package org.chorus.command.defaults

import org.chorus.Player
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.tree.node.PlayersNode
import org.chorus.command.utils.CommandLogger
import org.chorus.lang.TranslationContainer
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
