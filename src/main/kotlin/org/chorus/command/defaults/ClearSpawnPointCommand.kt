package org.chorus.command.defaults

import org.chorus.Player
import org.chorus.Server
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.tree.node.PlayersNode
import org.chorus.command.utils.CommandLogger
import org.chorus.network.protocol.types.SpawnPointType
import java.util.stream.Collectors

class ClearSpawnPointCommand(name: String) : VanillaCommand(name, "commands.clearspawnpoint.description") {
    init {
        this.permission = "chorus.command.clearspawnpoint"
        commandParameters.clear()
        this.addCommandParameters(
            "default", arrayOf(
                CommandParameter.newType("player", true, CommandParamType.TARGET, PlayersNode()),
            )
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
        var players = if (sender.isPlayer) listOf(sender.asPlayer()!!) else null
        if (list.hasResult(0)) players = list.getResult(0)
        if (players.isNullOrEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        for (player in players) {
            player.setSpawn(Server.instance.defaultLevel!!.spawnLocation, SpawnPointType.WORLD)
        }
        val playersStr = players.stream().map { obj: Player -> obj.name }.collect(Collectors.joining(" "))
        if (players.size > 1) {
            log.addSuccess("commands.clearspawnpoint.success.multiple", playersStr)
        } else {
            log.addSuccess("commands.clearspawnpoint.success.single", playersStr)
        }
        log.successCount(players.size).output()
        return players.size
    }
}
