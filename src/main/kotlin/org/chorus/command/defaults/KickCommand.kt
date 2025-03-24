package org.chorus.command.defaults

import org.chorus.Player
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.tree.node.PlayersNode
import org.chorus.command.utils.CommandLogger
import org.chorus.event.player.PlayerKickEvent
import kotlin.collections.set


class KickCommand(name: String) : VanillaCommand(name, "commands.kick.description") {
    init {
        this.permission = "nukkit.command.kick"
        commandParameters.clear()
        commandParameters["default"] = arrayOf(
            CommandParameter.Companion.newType("player", CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newType("reason", true, CommandParamType.MESSAGE)
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList>,
        log: CommandLogger
    ): Int {
        val list = result.value
        val players = list!!.getResult<List<Player>>(0)!!
        if (players.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        var reason: String? = ""
        if (list.hasResult(1)) {
            reason = list.getResult(1)
        }

        for (player in players) {
            player.kick(PlayerKickEvent.Reason.KICKED_BY_ADMIN, reason)
            if (reason!!.length >= 1) {
                log.addSuccess("commands.kick.success.reason", player.name, reason.toString())
            } else {
                log.addSuccess("commands.kick.success", player.name)
            }
        }
        log.successCount(players.size).output(true)
        return players.size
    }
}
