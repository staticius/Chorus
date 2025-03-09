package org.chorus.command.defaults

import cn.nukkit.Player
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.tree.node.PlayersNode
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.event.player.PlayerKickEvent
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/11
 */
class KickCommand(name: String) : VanillaCommand(name, "commands.kick.description") {
    init {
        this.permission = "nukkit.command.kick"
        commandParameters.clear()
        commandParameters["default"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("player", CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newType("reason", true, CommandParamType.MESSAGE)
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
