package org.chorus.command.defaults

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.tree.node.PlayersNode
import cn.nukkit.command.utils.CommandLogger
import java.util.List
import java.util.stream.Collectors

class ClearSpawnPointCommand(name: String) : VanillaCommand(name, "commands.clearspawnpoint.description") {
    init {
        this.permission = "nukkit.command.clearspawnpoint"
        getCommandParameters().clear()
        this.addCommandParameters(
            "default", arrayOf<CommandParameter?>(
                CommandParameter.Companion.newType("player", true, CommandParamType.TARGET, PlayersNode()),
            )
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
        var players = if (sender.isPlayer) List.of(sender.asPlayer()) else null
        if (list!!.hasResult(0)) players = list.getResult(0)
        if (players == null || players.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        for (player in players) {
            player.setSpawn(Server.getInstance().defaultLevel.spawnLocation, SpawnPointType.WORLD)
        }
        val players_str = players.stream().map { obj: Player -> obj.name }.collect(Collectors.joining(" "))
        if (players.size > 1) {
            log.addSuccess("commands.clearspawnpoint.success.multiple", players_str)
        } else {
            log.addSuccess("commands.clearspawnpoint.success.single", players_str)
        }
        log.successCount(players.size).output()
        return players.size
    }
}
