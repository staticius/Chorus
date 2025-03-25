package org.chorus.command.defaults

import org.chorus.IPlayer
import org.chorus.Server
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.tree.node.IPlayersNode
import org.chorus.command.utils.CommandLogger
import kotlin.collections.set

class PardonCommand(name: String) : VanillaCommand(name, "unban a player") {
    init {
        this.permission = "nukkit.command.unban.player"
        this.aliases = arrayOf("unban")
        commandParameters.clear()
        commandParameters["default"] = arrayOf(
            CommandParameter.newType("player", CommandParamType.TARGET, IPlayersNode())
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList>,
        log: CommandLogger
    ): Int {
        val players = result.value.getResult<List<IPlayer>>(0)!!
        if (players.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        for (player in players) {
            Server.instance.bannedPlayers.remove(player.name!!)
            log.addSuccess("commands.unban.success", player.name!!)
        }
        log.successCount(players.size).output(true)
        return players.size
    }
}
